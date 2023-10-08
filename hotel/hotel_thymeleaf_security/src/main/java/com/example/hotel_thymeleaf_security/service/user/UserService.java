package com.example.hotel_thymeleaf_security.service.user;


import com.example.hotel_thymeleaf_security.dto.request.AuthDto;
import com.example.hotel_thymeleaf_security.dto.request.ForgotDto;
import com.example.hotel_thymeleaf_security.dto.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;

public interface UserService extends BaseService<UserEntity, UserRequestDto> {

    UserEntity login(AuthDto auth);
    UserEntity getByEmail(String email);

    UserEntity forgotPassword(ForgotDto forgotDto);
}
