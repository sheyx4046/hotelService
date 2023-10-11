package com.example.hotel_thymeleaf_security.entity.dtos;

import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.entity.user.States;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String password;
    private String email;
    private Role roles;
    private States state;
}
