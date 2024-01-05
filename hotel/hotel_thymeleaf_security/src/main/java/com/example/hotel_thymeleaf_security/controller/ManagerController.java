package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.booking.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.VillageResponseDto;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import com.example.hotel_thymeleaf_security.service.village.OrderService;
import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/manager")
@PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('SUPER_ADMIN')")
@RequiredArgsConstructor
class ManagerController {
    private final VillageService villageService;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping()
    public String managerDashboard(){
        return "manager/index";
    }
    @GetMapping("/add/village")
    public String addVillagePage(){
        return "manager/add";
    }

    @PostMapping("/add/village")
    public String addHotel(@ModelAttribute VillageResponseDto dto,
                           Principal principal,
                           Model model){
        villageService.save(dto, principal.getName());
        return "redirect:/manager";
    }

    @GetMapping("/getVillages")
    public String getOwnerVillages(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Principal principal
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<VillaRentEntity> villas = villageService.getVillageByOwnerEmail(PageRequest.of(currentPage - 1, pageSize), principal.getName());
        model.addAttribute("villas", villas);
        int totalPages = villas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("user", userService);
        }
        return "manager/list";
    }

    @GetMapping("/edit")
    public String getDefaultEditPage(
            Principal principal
    ){
        VillaRentEntity lastVillage = villageService.getLastVillage(principal.getName());
        return "redirect:/manager/"+lastVillage.getId().toString()+"/edit";
    }

    @GetMapping("/{villaId}/edit")
    public String villageEditPage(Model model,
                                @PathVariable UUID villaId,
                                Principal principal){
        model.addAttribute("village", villageService.getById(villaId));
        model.addAttribute("villageService", villageService);
        return "manager/edit";
    }

    @PostMapping("/{villaId}/edit")
    public String villageEditPost(@PathVariable UUID villaId,
                                  @ModelAttribute VillageResponseDto villageResponseDto,
                                  Principal principal){
        villageService.update(villageResponseDto, villaId, principal.getName());
        return "manager/edit";
    }

    @GetMapping("/{villaId}/delete")
    public String villageDelete(@PathVariable UUID villaId,
                                Principal principal){
        villageService.deleteByIdAndUser(villaId, principal.getName());
        return "redirect:/manager/getVillages";
    }

    @GetMapping("/orders")
    public String ordersPage(Principal principal,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             Model model){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<OrderEntity> orderEntities = orderService.getOrderedPageOwner(PageRequest.of(currentPage - 1, pageSize), principal.getName());
        model.addAttribute("orders", orderEntities);
        int totalPages = orderEntities.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("user", userService);
            model.addAttribute("villageService", villageService);
        }
        return "manager/orders";
    }
    @GetMapping("/order/{orderId}/cancel")
    public String cancelOrder(Principal principal,
                              @PathVariable UUID orderId
                              ){

        return "";
    }
}