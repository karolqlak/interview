package org.ing.interview.web.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class FindRealEstateStatsQuery {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String size;
    private String types;
    private String dateSince;
    private String dateUntil;

    public BigDecimal getSizeFrom() {
        return BigDecimal.valueOf(Size.valueOf(size).getFrom());
    }

    public BigDecimal getSizeTo() {
        return BigDecimal.valueOf(Size.valueOf(size).getTo());
    }

    public LocalDate getDateFrom() {
        return LocalDate.parse(dateSince, DATE_FORMATTER);
    }

    public LocalDate getDateTo() {
        return LocalDate.parse(dateUntil, DATE_FORMATTER);
    }

    @AllArgsConstructor
    @Getter
    public enum Size {
        S(18, 45),
        M(46, 80),
        L(81, 400);
        private final int from;
        private final int to;
    }
}
