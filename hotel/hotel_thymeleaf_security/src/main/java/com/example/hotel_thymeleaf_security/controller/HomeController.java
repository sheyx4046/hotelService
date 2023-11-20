package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.dtos.FormDto;
import com.example.hotel_thymeleaf_security.entity.form.Form;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.user.form.FormService;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final VillageService villageService;
    private final FormService formService;

    @GetMapping("/test")
    public String test(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        return "hotel";
    }

    @GetMapping("/home")
    public String homePage(){
        return "villagePages/index";
    }

    @GetMapping("/")
    public String indexPage(
    ) {
        return "villagePages/index";
    }

    @GetMapping("/index")
    public String registeredPage(
            Principal principal
    ){
        try{
        UserEntity user = userService.getByEmail(principal.getName());
        switch (user.getRole()){
            case ADMIN, SUPER_ADMIN -> {return "redirect:/admin";}
            case MANAGER -> {return "redirect:/manager";}
            default -> {return "redirect:/";}
        }}catch (NullPointerException e){
            return "redirect:/";
        }
    }

    @GetMapping("/find")
    public String findPage(
            Model model,
            @RequestParam("page")Optional<Integer> page,
            @RequestParam("size")Optional<Integer> size
            ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);
        Page<VillaRentEntity> villas = villageService.getAllPage(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("villas", villas);
        int totalPages = villas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "villagePages/find";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "villagePages/contact";
    }

    @GetMapping("/booking")
    public String bookingPage() {
        return "villagePages/booking";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "villagePages/about";
    }



    @PostMapping("/form")
    public String userPage(@ModelAttribute FormDto form,
                           Model model
    ){
        formService.create(form);
        model.addAttribute("msg", "OK");
        return "redirect:/home";
    }
}
