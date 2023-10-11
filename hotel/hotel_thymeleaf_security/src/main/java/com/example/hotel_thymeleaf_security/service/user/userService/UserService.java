package com.example.hotel_thymeleaf_security.service.user.userService;


import com.example.hotel_thymeleaf_security.entity.dtos.AuthDto;
import com.example.hotel_thymeleaf_security.entity.dtos.ForgotDto;
import com.example.hotel_thymeleaf_security.entity.dtos.UserDto;
import com.example.hotel_thymeleaf_security.entity.dtos.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserService extends BaseService<UserEntity, UserRequestDto> {

    UserEntity login(AuthDto auth);
    UserEntity getByEmail(String email);
    UserEntity create(UserRequestDto userRequestDto, Boolean sendMessage);

    UserEntity forgotPassword(ForgotDto forgotDto);
    UserEntity update(UserDto userDto,  UUID userId);

    Boolean verifyCode(UUID userId);
    Boolean newVerifyCode(String email) throws MessagingException, UnsupportedEncodingException;
}
