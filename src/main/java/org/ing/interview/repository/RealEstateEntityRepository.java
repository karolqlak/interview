package org.ing.interview.repository;

import org.ing.interview.model.Region;
import org.ing.interview.model.entity.RealEstateInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RealEstateEntityRepository extends JpaRepository<RealEstateInfoEntity, Integer> {

    @Query("select avg(r.price) from RealEstateInfoEntity r where r.area >= ?1 and r.area <= ?2 and r.type in ?3 and r.date >= ?4 and r.date <= ?5 and r.region = ?6")
    BigDecimal countAveragePriceForCriteria(BigDecimal areaFrom, BigDecimal areaTo, String[] types, LocalDate from, LocalDate to, Region region);

}
