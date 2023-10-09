package com.example.hotel_thymeleaf_security.controller;


import com.example.hotel_thymeleaf_security.dto.request.AuthDto;
import com.example.hotel_thymeleaf_security.dto.request.ForgotDto;
import com.example.hotel_thymeleaf_security.dto.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("msg","OK");
        return "auth-templates/auth-login-basic";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute AuthDto authDto, HttpServletResponse response,
            Model model
    ) {
        UserEntity user = userService.login(authDto);
        if(user!=null){
            Cookie cookie=new Cookie("userId",user.getId().toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("error", "OK");
            if(user.getRoles().equals(Role.ADMIN)){
                return "redirect:/auth/admin";
            }else {
                return "redirect:/auth/user";}}
        model.addAttribute("msg","BAD");
        return "auth-templates/auth-login-basic";
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
    public String registerPage(Model model) {
        model.addAttribute("msg", "OK");
        return "auth-templates/auth-register-basic";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute UserRequestDto userRequestDto,
            Model model
    ) {
        if(userService.create(userRequestDto)!=null){
            return "auth-templates/auth-login-basic";
        }
            model.addAttribute("msg","BAD");
            return "auth-templates/auth-register-basic";
    }
    @GetMapping("/forgotPassword")
    public String forgotPasswordPage(){
        return "auth-templates/auth-forgot-password-basic";
    }
    @PostMapping("forgotPassword")
    public String forgotPassword(@ModelAttribute ForgotDto forgotDto){
        userService.forgotPassword(forgotDto);
        return "menu";}

    @GetMapping("/logout")
    public String logOutUser(HttpServletResponse response){
        Cookie cookie = new Cookie("userId", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "auth-templates/auth-login-basic";
    }

    @GetMapping("/cookie")
    public String getCookieValue(@CookieValue(name = "userId", required = false) String userId, Model model) {
        if (userId != null) {
            model.addAttribute("userId", userId);
            return "cook";
        } else {
            return "cook";
        }
    }
}
