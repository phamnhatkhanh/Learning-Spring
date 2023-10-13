package com.springbootmicroservices.advertisement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "Advertisement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement extends IdBasedEntity implements Serializable {

    private Long userId;
    private String title;
    private BigDecimal price;
    private Long viewCount;

    @Enumerated(EnumType.STRING)
    private AdvertisementState state;

}
