package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.village.FileService;
import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/village")
@RequiredArgsConstructor
public class VillageController {
    private final VillageService villageService;
    private final FileService fileService;
    @GetMapping()
    public String aboutVilla(
            @RequestParam("village") UUID village,
            Model model
    ){
        VillaRentEntity villaRentEntity = villageService.getById(village);
        model.addAttribute("villa", villaRentEntity);
        model.addAttribute("fileService", fileService);
        model.addAttribute("service", villageService);
        return "villagePages/villa";
    }

}
