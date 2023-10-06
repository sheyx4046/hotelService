package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.config.CookieValue;
import com.example.hotel_thymeleaf_security.dto.HotelRequestDto;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.RoomAmenity;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.RoomType;
import com.example.hotel_thymeleaf_security.service.hotel.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @GetMapping("/hotels/search")
    public String hotelFilter(){return "";}
    @GetMapping("/hotels/search/filter")
    public String hotelFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<RoomType> roomTypes,
            @RequestParam(required = false) List<RoomAmenity> roomAmenities,
            @RequestParam(required = false) double priceRangeMin,
            @RequestParam(required = false) double priceRangeMax,
            @RequestParam(required = false) boolean availability,
            @RequestParam(required = false) Boolean cancellationPolicy,
            @RequestParam(required = false) boolean petFriendly,
            @RequestParam(required = false) boolean parkingAvailable,
            @RequestParam(required = false) UUID managerOfHotel){
        return "";}


}
