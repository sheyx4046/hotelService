package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VillageResponseDto {
    private String name, city, roomAmenities, description, phoneNumber, email,
            instagram, facebook, youtube, telegram, googleMap;
    private double price;
    private boolean creditCard, cash;
    private int roomAmount;
    private MultipartFile generalImage, otherImage;
}
