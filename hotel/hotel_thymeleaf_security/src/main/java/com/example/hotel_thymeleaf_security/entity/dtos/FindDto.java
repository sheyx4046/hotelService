package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.Data;

@Data
public class FindDto {
    String from, to, city,minPrice, maxPrice;
}
