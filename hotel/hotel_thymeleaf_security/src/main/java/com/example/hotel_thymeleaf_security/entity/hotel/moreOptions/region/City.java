package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.region;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "cities")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class City extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "city")
    private List<HotelEntity> hotels;

}
