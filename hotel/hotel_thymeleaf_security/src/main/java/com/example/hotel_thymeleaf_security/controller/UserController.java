package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.booking.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.NewPasswordDto;
import com.example.hotel_thymeleaf_security.entity.dtos.UserDetailsDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import com.example.hotel_thymeleaf_security.service.village.OrderService;
import com.example.hotel_thymeleaf_security.service.village.OrderServiceImpl;
import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('SUPER_ADMIN') or hasRole('USER')")
public class UserController {
    private final UserService userService;
    private final VillageService villageService;
    private final OrderService orderService;

    @GetMapping()
    public String getUserDetailsPage(
            Principal principal,
            Model model
    ){
        model.addAttribute("user", userService.getByEmail(principal.getName()));
        return "user/edit-user-details";
    }

    @PostMapping()
    public String postUserDetailsPage(
            Principal principal,
            @ModelAttribute UserDetailsDto detailsDto,
            Model model
    ){
        UserEntity update = userService.update(detailsDto, userService.getByEmail(principal.getName()).getId());
        if(update!=null){
            model.addAttribute("user",update);
            return "user/edit-user-details";
        }
        return "redirect:/user";
    }

    @GetMapping("/newPassword")
    public String getNewPasswordPage(){
        return "user/edit-new-password";
    }

    @PostMapping("/newPassword")
    public String postNewPasswordPage(
            Principal principal,
            @ModelAttribute NewPasswordDto newPasswordDto,
            Model model
    ){
        model.addAttribute("user", userService.forgotPassword(
                principal.getName(),userService.getByEmail(principal.getName()).getId(),newPasswordDto));
        return "user/edit-user-details";
    }


}
