package org.ing.interview.schedule.repository;

import org.ing.interview.schedule.model.RealEstateImportJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RealEstateJobRepository extends JpaRepository<RealEstateImportJob, Integer> {
    List<RealEstateImportJob> findByDate(LocalDate date);
}
