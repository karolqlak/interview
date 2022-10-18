package org.ing.interview.service;

import lombok.RequiredArgsConstructor;
import org.ing.interview.model.Region;
import org.ing.interview.repository.RealEstateEntityRepository;
import org.ing.interview.web.query.FindRealEstateStatsQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RealEstateStatsService {

    private final RealEstateEntityRepository realEstateEntityRepository;

    public BigDecimal countAveragePrice(String regionId, FindRealEstateStatsQuery query) {
        return realEstateEntityRepository.countAveragePriceForCriteria(query.getSizeFrom(), query.getSizeTo(), query.getTypes().split(","), query.getDateFrom(), query.getDateTo(), Region.valueOf(regionId));
    }
}
