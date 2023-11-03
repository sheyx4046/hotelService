package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.bookin.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.BookingDto;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.village.OrderService;
import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/addOrder")
    public ResponseEntity<OrderEntity> addOrder(
            @RequestBody BookingDto bookingDto
    ){
        OrderEntity orderEntity = orderService.addOrder(bookingDto);
        return ResponseEntity.ok(orderEntity);
    }
    @PostMapping("/findVilla")
    public String findVilla(
            @RequestParam String city,
            @RequestParam LocalDate starDate,
            @RequestParam LocalDate endDate,
            Model model ){
        List<VillaRentEntity> byCityAndDate = orderService.findByCityAndDate(city, starDate, endDate);
        model.addAttribute("villas",byCityAndDate);
        return "villagePages/find";
    }
    @GetMapping("/getAll")
    public String villaAllPage(
            Model model
    ){
        model.addAttribute("villas",orderService.getAll());
        return "villagePages/find";
    }
}
