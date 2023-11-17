package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/village")
@RequiredArgsConstructor
public class VillageController {
    private final VillageService villageService;
    @GetMapping("/{villaId}/info")
    public String getVillagePage(
            Model model,
            @PathVariable UUID villaId
            ){
        model.addAttribute("villa", villageService.getById(villaId));
        return "/";
    }

    @GetMapping("")
    public String getVillagesPage(){
        return "/";
    }
}
