package org.ing.interview.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.ing.interview.model.Region;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "REAL_ESTATE_INFO")
@Getter
@Setter
public class RealEstateInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;
    private BigDecimal price;
    @Column(length = 500)
    private String description;
    private BigDecimal area;
    private int rooms;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private Region region;
}
