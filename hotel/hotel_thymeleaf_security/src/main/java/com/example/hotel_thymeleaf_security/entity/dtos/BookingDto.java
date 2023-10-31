package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDto {
    public UUID villaId;

    public UUID userId;

    public LocalDate startDay;

    public LocalDate endDay;


    public Double price;

}
