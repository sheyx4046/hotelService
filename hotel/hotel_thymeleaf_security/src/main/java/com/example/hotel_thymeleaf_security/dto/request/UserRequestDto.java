package com.example.hotel_thymeleaf_security.dto.request;

import com.example.hotel_thymeleaf_security.entity.user.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequestDto {
    @NotBlank(message = "name must not be blank")
    private String username;
    @NotBlank(message = "email must not be blank")
    private String email;
    @NotBlank(message = "password must not be blank")
    private String password;
    private Role roles=Role.USER;
}
