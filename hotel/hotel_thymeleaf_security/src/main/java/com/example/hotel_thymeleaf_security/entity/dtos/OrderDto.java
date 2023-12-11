package com.example.hotel_thymeleaf_security.entity.dtos;

import com.example.hotel_thymeleaf_security.entity.booking.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID userId;
    private UUID hotelId;
    private Double price;
    private LocalDate startDay;
    private LocalDate endDay;
    private BookingStatus bookingStatus;
}
