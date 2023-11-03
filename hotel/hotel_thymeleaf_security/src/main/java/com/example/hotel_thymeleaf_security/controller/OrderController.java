package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.bookin.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.BookingDto;
import com.example.hotel_thymeleaf_security.entity.dtos.FindDto;
import com.example.hotel_thymeleaf_security.service.village.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
            @ModelAttribute FindDto findDto,
            Model model ){
//        List<VillaRentEntity> byCityAndDate = orderService.findByCityAndDate(findDto);
//        model.addAttribute("villas",byCityAndDate);
        return "villagePages/find";
    }
    @GetMapping("/getAll")
    public String villaAllPage(
            Model model
    ){
//        model.addAttribute("villas",orderService.getAll());
        return "villagePages/find";
    }
}
