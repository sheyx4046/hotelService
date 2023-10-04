package com.example.hotel_thymeleaf_security.exception;

public class UniqueObjectException extends RuntimeException{

    public UniqueObjectException(String message){
        super(message);
    }
}
