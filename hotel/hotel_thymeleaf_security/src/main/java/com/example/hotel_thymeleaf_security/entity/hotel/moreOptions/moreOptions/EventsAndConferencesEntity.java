package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

import java.util.List;

@Entity(name = "event_conferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventsAndConferencesEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String event;

    @JsonIgnore
    @ManyToMany(mappedBy = "eventsAndConferences")
    private List<HotelEntity> hotels;
}
