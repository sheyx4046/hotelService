package com.example.hotel_thymeleaf_security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN', 'USER')")
public class HomeController {

    @GetMapping("/test")
    public String home(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        return "hotel";
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
