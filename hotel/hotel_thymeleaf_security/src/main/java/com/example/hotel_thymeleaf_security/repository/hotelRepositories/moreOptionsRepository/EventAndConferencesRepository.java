package com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository;

import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.EventsAndConferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventAndConferencesRepository extends JpaRepository<EventsAndConferencesEntity, UUID> {
    Optional<EventsAndConferencesEntity> findEventsAndConferencesEntitiesByEvent(String event);
}
