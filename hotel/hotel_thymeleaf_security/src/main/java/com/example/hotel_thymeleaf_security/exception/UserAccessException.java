package com.example.hotel_thymeleaf_security.exception;

public class UserAccessException extends RuntimeException {
    public UserAccessException(String s) {
        super(s);
    }
}
