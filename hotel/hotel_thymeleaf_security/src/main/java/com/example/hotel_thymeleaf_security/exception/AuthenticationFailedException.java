package com.example.hotel_thymeleaf_security.exception;


public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message){
        super(message);
    }
 }
