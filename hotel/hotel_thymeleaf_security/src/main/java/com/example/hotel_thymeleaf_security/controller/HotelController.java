package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.config.CookieValue;
import com.example.hotel_thymeleaf_security.dto.HotelRequestDto;
import com.example.hotel_thymeleaf_security.service.hotel.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    @GetMapping("/add")
    public String hotelPage(){
        return "hotel";
    }
    @PostMapping("/add")
    public String addHotel(@ModelAttribute HotelRequestDto hotelRequestDto,
//                           @RequestParam Integer amount,
//                           @RequestParam MultipartFile image,
                           HttpServletRequest request,
                           Model model){
        String ownerValue = CookieValue.getValue("userId", request);
        Long owner = Long.parseLong(ownerValue);
        System.out.println(owner);
        hotelService.save(hotelRequestDto,owner);
        model.addAttribute("message","hotel successfully added");
        return "admin";
    }
}
