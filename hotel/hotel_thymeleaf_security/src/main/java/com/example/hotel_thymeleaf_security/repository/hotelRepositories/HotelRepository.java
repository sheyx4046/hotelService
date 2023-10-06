package com.example.hotel_thymeleaf_security.repository.hotelRepositories;


import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    Optional<HotelEntity> findHotelEntityByName(String name);

    @Query("SELECT h FROM hotels h " +
            "JOIN h.city c " +
            "WHERE h.parkingAvailable = :parking " +
            "AND h.availability = :available " +
            "AND h.petFriendly = :petFriendly " +
            "AND h.priceRangeMin >= :minPrice " +
            "AND h.priceRangeMax <= :maxPrice " +
            "AND c.name = :cityName " +
            "AND c.country.nameCountry = :countryName")
    Page<HotelEntity> findHotelsWithFilters(
            @Param("parking") boolean parking,
            @Param("available") boolean available,
            @Param("petFriendly") boolean petFriendly,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            @Param("cityName") String cityName,
            @Param("countryName") String countryName,
            Pageable pageable);


}
