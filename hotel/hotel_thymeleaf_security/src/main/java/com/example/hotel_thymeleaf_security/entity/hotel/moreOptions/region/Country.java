package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.region;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity(name = "countries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Country extends BaseEntity {
    @Column(nullable = true, unique = true)
    private String nameCountry;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private List<City> cities;
}
