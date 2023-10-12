package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.Data;


@Data
public class MailDto {
    private String message;
    private String toUser;
}
