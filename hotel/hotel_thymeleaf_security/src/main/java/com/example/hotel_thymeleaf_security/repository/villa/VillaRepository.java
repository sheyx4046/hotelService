package com.example.hotel_thymeleaf_security.repository.villa;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VillaRepository extends JpaRepository<VillaRentEntity, UUID> {
    Optional<List<VillaRentEntity>> findByCity(String city);
    Optional<VillaRentEntity> findByNameAndOwnerId(String villageName, UUID ownerId);
    Page<VillaRentEntity> findAllByOrderByCreatedDate(Pageable pageable);
    List<VillaRentEntity> findVillaRentEntitiesByOwnerIdOrderByCreatedDate(UUID ownerId);
    List<VillaRentEntity> findVillaRentEntitiesByOwnerId(UUID ownerId);

}
