package com.example.hotel_thymeleaf_security.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForgotDto {
    private String email;
    private String code;
    private String newPas;
    private String newPas2;

}
