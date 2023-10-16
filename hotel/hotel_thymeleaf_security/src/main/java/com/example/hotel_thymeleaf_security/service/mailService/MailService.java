package com.example.hotel_thymeleaf_security.service.mailService;

import com.example.hotel_thymeleaf_security.entity.dtos.MailDto;
import com.example.hotel_thymeleaf_security.entity.mail.MailEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MailService extends BaseService<MailEntity, MailDto> {
    void sendMessageToUser(String toUserEmail) throws MessagingException, UnsupportedEncodingException;
    void sendForgotPassword(String email) throws MessagingException, UnsupportedEncodingException;
}
