package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.Data;

@Data
public class NewPasswordDto {
    private String password, newPassword1,newPassword2;
}
