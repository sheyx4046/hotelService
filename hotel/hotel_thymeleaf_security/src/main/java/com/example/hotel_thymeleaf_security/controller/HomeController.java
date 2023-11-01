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
    public String test(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        return "hotel";
    }

    @GetMapping("/home")
    public String homePage() {
        return "villagePages/index";
    }

    @GetMapping("/find")
    public String findPage() {
        return "villagePages/find";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "villagePages/contact";
    }

    @GetMapping("/booking")
    public String bookingPage() {
        return "villagePages/booking";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "villagePages/about";
    }



//    @GetMapping("/user")
//    public String userPage(){
//        return "user";
//    }
}
