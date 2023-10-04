package com.example.hotel_thymeleaf_security.controller;


import com.example.hotel_thymeleaf_security.dto.request.AuthDto;
import com.example.hotel_thymeleaf_security.dto.request.ForgotDto;
import com.example.hotel_thymeleaf_security.dto.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute AuthDto authDto, HttpServletResponse response
    ) {
        UserEntity user = userService.getByEmail(authDto.getEmail());
        Cookie cookie=new Cookie("userId",user.getId().toString());
        cookie.setPath("/");
        response.addCookie(cookie);
        if(user.getRoles().equals(Role.ADMIN)){
            return "redirect:/auth/admin";
        }
       return "redirect:/auth/user";
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }
    @GetMapping("/user")
    public String userPage(){
        return "user";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute UserRequestDto userRequestDto
    ) {
        UserEntity userEntity = userService.create(userRequestDto);
        return "index";
    }
    @GetMapping("forgortPassword")
    public String forgotPasswordPage(){
        return "forgortPassword";
    }
      @PostMapping("forgortPassword")
    public String forgortPssword(@ModelAttribute ForgotDto forgotDto){
        userService.forgotPassowrd(forgotDto);
        return  "menu";
      }
}
