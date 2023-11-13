package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.dtos.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
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
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping()
    public String dashboard() {
        return "manager/index";
    }

    @GetMapping("/users-list")
    public String usersListPage(
        Model model,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size,
        Principal principal
    ) {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(10);
            Page<UserEntity> users = userService.getAllPage(PageRequest.of(currentPage - 1, pageSize));
            model.addAttribute("users", users);
            int totalPages = users.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            return "admin";
        }


    @GetMapping("/edit")
    public String editRedirect(){
        UserEntity lastUser = userService.getLastUser();
        return "redirect:/admin"+lastUser.getId().toString()+"/edit";
    }

    @PostMapping("/{userId}/edit")
    public String editPagePost(Model model,
                           @ModelAttribute UserRequestDto userInfo, @PathVariable UUID userId){
        userService.update(userInfo, userId);
        return "admin";
    }

    @GetMapping("/{userId}/edit")
    public String editPage(Model model,
                           @PathVariable UUID userId){
        model.addAttribute("user", userService.getById(userId));
        return "admin/edit";
    }

    @DeleteMapping("/{userId}/delete")
    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    public String deleteUser(@PathVariable UUID userId){
        userService.deleteById(userId);
        return "admin";
    }

    @GetMapping("/add")
    public String addUserPage(){
        return "admin/add-user";
    }

}
