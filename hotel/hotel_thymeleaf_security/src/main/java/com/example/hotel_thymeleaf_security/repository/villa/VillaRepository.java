package com.example.hotel_thymeleaf_security.repository.villa;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VillaRepository extends JpaRepository<VillaRentEntity, UUID> {
    Optional<List<VillaRentEntity>> findByCity(String city);
}
