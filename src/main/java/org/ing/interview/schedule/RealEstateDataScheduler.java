package org.ing.interview.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ing.interview.integration.RealEstateApiClient;
import org.ing.interview.integration.model.PagedResponse;
import org.ing.interview.model.Region;
import org.ing.interview.schedule.model.RealEstateImportJob;
import org.ing.interview.schedule.repository.RealEstateJobRepository;
import org.ing.interview.service.TimeProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealEstateDataScheduler {

    private final RealEstateDataSchedulerExecutor jobExecutor;
    private final RealEstateJobRepository jobRepository;
    private final TimeProvider timeProvider;

    @Scheduled(cron = "${realestate.scheduler.cron}")
    public void loadRealEstateData() {
        log.debug("Running RealEstateDataScheduler for date: {}", timeProvider.now());
        List<RealEstateImportJob> allImportsToday = jobRepository.findByDate(timeProvider.now());
        log.debug("allImportsToday({})", allImportsToday.size());
        Arrays.stream(Region.values())
                .filter(region -> checkImportForRegion(region, allImportsToday))
                .forEach(jobExecutor::runImportForRegion);
    }

    private boolean checkImportForRegion(Region region, List<RealEstateImportJob> allImportsToday) {
        log.debug("Checking import for region: {}", region);
        boolean exists = existsByRegion(region, allImportsToday);
        if (exists) {
            log.debug("There is already import for that region {} today, not running", region);
            return false;
        }
        return true;
    }

    private boolean existsByRegion(Region region, List<RealEstateImportJob> allImportsToday) {
        return allImportsToday.stream().anyMatch(job -> job.getRegion() == region);
    }

}
