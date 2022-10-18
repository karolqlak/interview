package org.ing.interview.schedule.model;

import lombok.Getter;
import lombok.Setter;
import org.ing.interview.model.Region;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class RealEstateImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Region region;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String reason;

    public enum Status {
        SUCCESS, FAILED;
    }
}
