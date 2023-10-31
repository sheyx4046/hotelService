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
    private String name, city, roomAmenities, description, phoneNumber, email, instagram, facebook, youtube, telegram;
    private double price;
    private boolean creditCard=false, cash=false;
    private int roomAmount;
    private String ownerEmail;
    private MultipartFile generalImage, otherImage;
}
