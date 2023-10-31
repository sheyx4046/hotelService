
package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.dtos.VillageResponseDto;
import com.example.hotel_thymeleaf_security.service.hotel.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('SUPER_ADMIN')")
@RequiredArgsConstructor
public class HotelManagerController {
    private final HotelService hotelService;
    @GetMapping()
    public String managerDashboard(){
        return "manager/dashboard";
    }
    @GetMapping("/add/hotel")
    public String addVillagePage(){
        return "manager/add-hotel";
    }

    @PostMapping("/add/hotel")
    public String addHotel(@ModelAttribute VillageResponseDto dto,
                           Model model){

        System.out.println(dto.getEmail());
        return "admin";
    }
}
