package com.example.hotel_thymeleaf_security.repository.booking;


import com.example.hotel_thymeleaf_security.entity.booking.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findAllByUserId(UUID userId);
//    Optional<List<OrderEntity>>findAllByRoomId(UUID roomId, Pageable pageable);
//    @Query(value = "select o from orders o where o.bookingStatus = 'BOOKED' OR o.roomId = :roomId")
//    Optional<List<OrderEntity>>findOrderEntitiesByBookingStatusRoomId(@Param(value = "roomId") UUID roomId);

    List<OrderEntity> findByBookingStatus(com.example.hotel_thymeleaf_security.entity.booking.BookingStatus bookingStatus);

    List<OrderEntity> findOrderEntitiesByVillaIdIn(List<UUID> villaId);
}

