package com.example.hotel_thymeleaf_security.repository.booking;


import com.example.hotel_thymeleaf_security.entity.booking.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Page<OrderEntity> findAllByUserId(UUID userId, Pageable pageable);
//    Optional<List<OrderEntity>>findAllByRoomId(UUID roomId, Pageable pageable);
    @Query(value = "select o from orders o where o.bookingStatus = 'BOOKED' OR o.roomId = :roomId")
    Optional<List<OrderEntity>>findOrderEntitiesByBookingStatusRoomId(@Param(value = "roomId") UUID roomId);

    List<OrderEntity> findByBookingStatus(com.example.hotel_thymeleaf_security.entity.booking.BookingStatus bookingStatus);
}

