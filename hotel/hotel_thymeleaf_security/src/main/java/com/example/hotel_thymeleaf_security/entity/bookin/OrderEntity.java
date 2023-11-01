package com.example.hotel_thymeleaf_security.entity.bookin;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity extends BaseEntity {
    private UUID userId;
    private UUID hotelId;

    public UUID roomId;

    private Double price;
    private LocalDate startDay;
    private LocalDate endDay;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;



}
