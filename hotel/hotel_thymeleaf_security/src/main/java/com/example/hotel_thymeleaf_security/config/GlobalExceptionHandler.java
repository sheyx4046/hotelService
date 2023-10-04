package com.example.hotel_thymeleaf_security.config;

import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.RequestValidationException;
import com.example.hotel_thymeleaf_security.exception.UniqueObjectException;
import com.example.hotel_thymeleaf_security.exception.UserBadRequestException;
import jakarta.mail.AuthenticationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataNotFoundException.class, InternalAuthenticationServiceException.class})
    public ModelAndView dataNotFoundExceptionHandler(HttpServletRequest request, DataNotFoundException e) {
        System.out.println(request.getRequestURL());
        System.out.println(request.getPathInfo());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
//    @ExceptionHandler(value = {RequestValidationException.class})
//    public ResponseEntity<String> requestValidationExceptionHandler(
//            RequestValidationException e
//    ){
//        return ResponseEntity.status(400).body(e.getMessage());
//    }
//
//    @ExceptionHandler(value = {AuthenticationFailedException.class})
//    public ResponseEntity<String> authenticationFailedExceptionHandler(
//            AuthenticationFailedException e
//    ){
//        return ResponseEntity.status(401).body(e.getMessage());
//    }
//
//    @ExceptionHandler(value = {UniqueObjectException.class})
//    public ResponseEntity<String> uniqueObjectException(
//            UniqueObjectException e){
//        return ResponseEntity.status(400).body(e.getMessage());
//    }
//
//    @ExceptionHandler(value = {ObjectNotFoundException.class})
//    public ResponseEntity<String> objectNotFoundExceptionHandler(
//            ObjectNotFoundException e){
//        return ResponseEntity.status(401).body(e.getMessage());
//    }
//
//    @ExceptionHandler(value = {DataNotFoundException.class})
//    public ResponseEntity<String> dataNotFoundExceptionHandler(
//            DataNotFoundException e){
//        return ResponseEntity.status(401).body(e.getMessage());
//    }
}
