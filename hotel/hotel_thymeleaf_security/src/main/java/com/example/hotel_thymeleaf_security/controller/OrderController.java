package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.bookin.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.BookingDto;
import com.example.hotel_thymeleaf_security.service.hotel.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

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
    @GetMapping("/findVilla")
    public String findVilla(String city, LocalDate starDate,LocalDate endDate){


    }
}
