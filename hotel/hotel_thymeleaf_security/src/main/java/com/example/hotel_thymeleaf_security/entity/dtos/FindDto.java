package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindDto {
    String from, to, city;
    Double minPrice, maxPrice;
}
