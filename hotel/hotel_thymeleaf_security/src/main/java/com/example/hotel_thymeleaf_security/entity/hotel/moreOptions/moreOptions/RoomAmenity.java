package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

import java.util.List;

@Entity(name = "room_amenities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomAmenity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String amenity;

    @JsonIgnore(value = true)
    @ManyToMany(mappedBy = "roomAmenities")
    private List<HotelEntity> hotelEntities;

}