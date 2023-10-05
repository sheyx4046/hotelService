package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

@Entity(name = "map_location")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MapLocation extends BaseEntity{
    private double latitude;
    private double longitude;
}
