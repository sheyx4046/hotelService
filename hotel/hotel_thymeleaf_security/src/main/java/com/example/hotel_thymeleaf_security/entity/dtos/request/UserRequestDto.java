package com.example.hotel_thymeleaf_security.entity.dtos.request;

import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.entity.user.States;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequestDto {
    @NotBlank(message = "name must not be blank")
    private String name;
    @NotBlank(message = "email must not be blank")
    private String email;
    @NotBlank(message = "password must not be blank")
    private String password;
    private Role roles=Role.USER;
    private States states=States.UNVERIFIED;
}
