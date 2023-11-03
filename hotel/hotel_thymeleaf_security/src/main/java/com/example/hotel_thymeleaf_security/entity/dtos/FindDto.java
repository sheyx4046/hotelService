package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FindDto {
    LocalDate from, to;
    String city;
    double minPrice, maxPrice;
}
