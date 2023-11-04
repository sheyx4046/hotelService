package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping("/test")
    public String test(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        return "hotel";
    }

    @GetMapping("/")
    public String homePage(
    ) {
        return "villagePages/index";
    }

    @GetMapping("/index")
    public String registeredPage(
            Principal principal
    ){
        try{
        UserEntity user = userService.getByEmail(principal.getName());
        switch (user.getRole()){
            case ADMIN, SUPER_ADMIN -> {return "redirect:/admin";}
            case MANAGER -> {return "redirect:/manager";}
            default -> {return "redirect:/";}
        }}catch (NullPointerException e){
            return "redirect:/";
        }
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
