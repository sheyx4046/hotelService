package com.example.hotel_thymeleaf_security.repository;


import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    Optional<HotelEntity> findHotelEntityByName(String name);
    Optional<HotelEntity> findHotelEntityByWebsite(String website);

}
