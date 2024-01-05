package com.example.hotel_thymeleaf_security.entity.booking;

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
    private UUID villaId;
    private Double totalPrice;
    private LocalDate startDay;
    private LocalDate endDay;
    private String description;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
}
