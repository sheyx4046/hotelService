package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthDto {
    private String username;
    private String password;
}
