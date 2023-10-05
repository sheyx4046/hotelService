package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

import java.util.List;

@Entity(name = "room_types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomType extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String typeName;
    private String description;

    @JsonIgnore(value = true)
    @ManyToMany(mappedBy = "roomTypes")
    private List<HotelEntity> hotel;
}
