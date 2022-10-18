package org.ing.interview.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ing.interview.integration.RealEstateApiClient;
import org.ing.interview.integration.model.PagedResponse;
import org.ing.interview.model.Region;
import org.ing.interview.model.entity.RealEstateInfoEntity;
import org.ing.interview.repository.RealEstateEntityRepository;
import org.ing.interview.schedule.model.RealEstateImportJob;
import org.ing.interview.schedule.repository.RealEstateJobRepository;
import org.ing.interview.service.TimeProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealEstateDataSchedulerExecutor {

    private final RealEstateApiClient realEstateApiClient;
    private final RealEstateEntityRepository realEstateEntityRepository;
    private final RealEstateJobRepository jobRepository;
    private final TimeProvider timeProvider;

    @Transactional
    public void runImportForRegion(Region region) {
        long currentPage = 1;
        log.debug("Running import for day: {} and region: {} and page: {}", region, timeProvider.now(), currentPage);
        try {
            PagedResponse response = realEstateApiClient.getPageForRegion(region.name(), currentPage);
            saveDataFromImport(response.getData(), region);
            long totalPages = response.getTotalPages();
            log.debug("There are {} pages for region: {} in date: {}, starting import...", totalPages, region, timeProvider.now());
            for (int i = 2; i <= response.getTotalPages(); i++) {
                log.debug("importing page {}/{}", i, totalPages);
                response = realEstateApiClient.getPageForRegion(region.name(), i);
                saveDataFromImport(response.getData(), region);
            }
            saveSuccessForRegion(region);
        } catch (Exception exc) {
            log.error("Error during import", exc);
            saveFailForRegion(region, exc.getMessage());
        }
    }

    private void saveDataFromImport(List<PagedResponse.RealEstateApiData> data, Region region) {
        List<RealEstateInfoEntity> entities = data.stream().map(d -> mapToEntity(d, region)).collect(Collectors.toList());
        realEstateEntityRepository.saveAll(entities);
    }

    private void saveFailForRegion(Region region, String causedBy) {
        RealEstateImportJob jobStatus = new RealEstateImportJob();
        jobStatus.setReason(causedBy);
        jobStatus.setDate(timeProvider.now());
        jobStatus.setStatus(RealEstateImportJob.Status.FAILED);
        jobStatus.setRegion(region);
        jobRepository.save(jobStatus);
    }

    private void saveSuccessForRegion(Region region) {
        RealEstateImportJob jobStatus = new RealEstateImportJob();
        jobStatus.setDate(timeProvider.now());
        jobStatus.setStatus(RealEstateImportJob.Status.SUCCESS);
        jobStatus.setRegion(region);
        jobRepository.save(jobStatus);
    }

    private RealEstateInfoEntity mapToEntity(PagedResponse.RealEstateApiData data, Region region) {
        RealEstateInfoEntity entity = new RealEstateInfoEntity();
        entity.setRegion(region);
        entity.setArea(new BigDecimal(data.getArea()));
        entity.setDescription(data.getDescription());
        entity.setRooms(data.getRooms());
        entity.setPrice(new BigDecimal(data.getPrice()));
        entity.setType(data.getType());
        entity.setDate(timeProvider.now());
        return entity;
    }
}
