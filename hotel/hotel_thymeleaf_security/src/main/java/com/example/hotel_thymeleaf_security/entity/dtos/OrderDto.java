package com.example.hotel_thymeleaf_security.entity.dtos;

import com.example.hotel_thymeleaf_security.entity.booking.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class OrderDto {
    private UUID userId;
    private UUID villaId;
    private LocalDate startDay;
    private LocalDate endDay;
    private BookingStatus bookingStatus;
}
