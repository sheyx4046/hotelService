package com.example.hotel_thymeleaf_security.entity.dtos;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
public class MailDto {
    private String message;
}
