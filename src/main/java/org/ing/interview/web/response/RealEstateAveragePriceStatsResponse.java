package org.ing.interview.web.response;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class RealEstateAveragePriceStatsResponse {
    private BigDecimal avgValue;
}
