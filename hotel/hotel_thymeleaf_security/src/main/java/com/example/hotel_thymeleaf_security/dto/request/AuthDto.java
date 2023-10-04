package com.example.hotel_thymeleaf_security.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthDto {
    private String username;
    private String password;
    private String email;
}
