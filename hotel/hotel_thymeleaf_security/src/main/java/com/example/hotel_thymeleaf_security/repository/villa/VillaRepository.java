package com.example.hotel_thymeleaf_security.repository.villa;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VillaRepository extends JpaRepository<VillaRentEntity, UUID> {
    Optional<List<VillaRentEntity>> findByCity(String city);
    Optional<VillaRentEntity> findByNameAndOwnerId(String villageName, UUID ownerId);
    Page<VillaRentEntity> findAllByOrderByCreatedDate(Pageable pageable);
    List<VillaRentEntity> findVillaRentEntitiesByOwnerIdOrderByCreatedDate(UUID ownerId);
    List<VillaRentEntity> findVillaRentEntitiesByOwnerId(UUID ownerId);

    @Query(value = "SELECT * FROM villa v " +
            "INNER JOIN orders o ON v.id = o.villa_id " +
            "WHERE (:city IS NULL OR v.city = :city) " +
            "AND (:minPrice IS NULL OR v.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR v.price <= :maxPrice) " +
            "AND NOT ((:from IS NOT NULL AND :to IS NOT NULL) AND (o.start_day BETWEEN :from AND :to OR o.end_day BETWEEN :from AND :to))",
            nativeQuery = true)
    List<VillaRentEntity> findHotelsBySearchCriteria(
            @Param("city") String city,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("from") String from,
            @Param("to") String to);
}
