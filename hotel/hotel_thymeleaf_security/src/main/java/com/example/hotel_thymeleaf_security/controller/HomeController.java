package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN', 'USER')")
public class HomeController {

    @GetMapping("/test")
    public String home() {
        throw new DataNotFoundException("hello");
    }

    @GetMapping("/user")
    public String hs() {
        return "user";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
//    @GetMapping("/user")
//    public String userPage(){
//        return "user";
//    }
}
