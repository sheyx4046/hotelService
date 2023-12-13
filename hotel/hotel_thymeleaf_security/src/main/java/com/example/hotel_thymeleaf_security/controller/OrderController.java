package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.booking.OrderEntity;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import com.example.hotel_thymeleaf_security.service.village.OrderService;
import com.example.hotel_thymeleaf_security.service.village.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.NoPermissionException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('SUPER_ADMIN') or hasRole('USER')")
public class OrderController{
    private final OrderService orderService;
    private final UserService userService;
    private final VillageService villageService;

    @GetMapping()
    public String getVillageListPage(
            Principal principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size")Optional<Integer> size,
            Model model
    ){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page<OrderEntity> order = orderService.getByOrderedPage(PageRequest.of(currentPage - 1, pageSize), principal.getName());
        model.addAttribute("userOrder", order);
        int totalPages = order.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("villa", villageService);
        }
        return "user/booked-villages";
    }

    @GetMapping("/delete")
    public String deleteVillage(Principal principal,
                                @RequestParam UUID villaId
    ) throws NoPermissionException {
        orderService.delete(villaId, principal.getName());
        return "redirect:/ordered";
    }

}
