package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.dtos.AuthDto;
import com.example.hotel_thymeleaf_security.entity.dtos.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.States;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.service.user.auth.AuthService;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticateManager;
    private final AuthService authService;
    @GetMapping
    public String login(){
        return "redirect:/auth/login";
    }
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("msg","OK");
        return "auth-templates/auth-login-basic";
    }

    @GetMapping("/whoami")
    public String whoami(Principal principal){
        return "user";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute AuthDto authDto, HttpServletResponse response,
//            Model model){
//        UserEntity user = userService.login(authDto);
//        Cookie cookie = new Cookie("userId", user.getId().toString());
//                cookie.setPath("/");
//                cookie.setMaxAge(3600*5);
//                response.addCookie(cookie);
//        model.addAttribute("user", user);
//                return "menu";
//    }


    @PostMapping("/login")
    public String login(
            @ModelAttribute AuthDto authDto, HttpServletResponse response,
            Model model
    )
            throws MessagingException, UnsupportedEncodingException {
        UserEntity user = userService.login(authDto);
        if(user!=null) {
            if(user.getState().equals(States.ACTIVE)){
//                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//                Authentication authentication = authenticateManager.authenticate(authenticationToken);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
                Cookie cookie = new Cookie("userId", user.getId().toString());
                cookie.setPath("/");
                cookie.setMaxAge(3600*5);
                response.addCookie(cookie);
                model.addAttribute("error", "OK");
                return "menu";} else if (user.getState().equals(States.UNVERIFIED)) {
                model.addAttribute("email", user.getEmail());
                userService.newVerifyCode(user.getEmail());
                return "auth-templates/auth-verification-password";
            }
        }
        model.addAttribute("error","BAD");
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
        if(userService.create(userRequestDto,true)!=null){
            model.addAttribute("email", userService.getByEmail(userRequestDto.getEmail()).getEmail());
            return "auth-templates/auth-verification-password";
        }
            model.addAttribute("msg","BAD");
            return "auth-templates/auth-register-basic";
    }

    @GetMapping("{id}/verify-code")
    public String verifyCodePage(
            @PathVariable UUID id
            ){
        userService.verifyCode(id);
        return "redirect:/auth/login";
    }

    @GetMapping("/new-verify-code")
    public String newVerifyCodePage(){
        return "auth-templates/auth-new-verification";
    }
    @PostMapping("/new-verify-code")
    public String newVerifyCode(
            @RequestParam("email") String email,
            Model model
    ) throws MessagingException, UnsupportedEncodingException {
        userService.newVerifyCode(email);
        model.addAttribute("email",userService.getByEmail(email).getEmail());
        return "auth-templates/auth-verification-password";
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordPage(){
        return "auth-templates/auth-forgot-password-basic";
    }
    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model) throws MessagingException, UnsupportedEncodingException {
        UserEntity user = userService.getByEmail(email);
        if(user==null){
            model.addAttribute("error", "BAD");
            return "auth-templates/auth-forgot-password-basic";
            }
        model.addAttribute("email", userService.getByEmail(email).getEmail());
        userService.sendForgotPassword(email);
        return "auth-templates/auth-forgot-password-message";
    }

    @GetMapping("{userEmail}/forgot-password-message")
    public String sendNewForgotPassword(
            @PathVariable String userEmail,
            Model model
    ) throws MessagingException, UnsupportedEncodingException {
        UserEntity user = userService.getByEmail(userEmail);
        if(user==null){
            model.addAttribute("error", "BAD");
            return "auth-templates/auth-forgot-password-basic";
        }
        model.addAttribute("email", userService.getByEmail(userEmail).getEmail());
        userService.sendForgotPassword(userEmail);
        return "auth-templates/auth-forgot-password-message";
    }

    @GetMapping("/{userId}/{userName}/set-new-password")
    public String changePasswordPage(
            @PathVariable String userId, @PathVariable String userName, Model model){
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        return "auth-templates/auth-create-new-password";
    }
    @PostMapping("/set-new-password")
    public String changePassword(
            @RequestParam String newPassword,
            @RequestParam UUID userId,
            @RequestParam String userName){
        userService.forgotPassword(userName,userId,newPassword);
        return "auth-templates/auth-login-basic";
    }

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

    @GetMapping("/error-page.html")
    public String errorPageLogin(Model model){
        model.addAttribute("error", "BAD");
        return "auth-templates/auth-login-basic";
    }

}
