package com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository;

import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoomTypesRepository extends JpaRepository<RoomType, UUID> {

    Optional<RoomType> findRoomTypeById(UUID id);
    Optional<RoomType> findRoomTypeByTypeName(String name);
}
