package com.example.hotel_thymeleaf_security.controller;

import com.example.hotel_thymeleaf_security.entity.dtos.UserDto;
import com.example.hotel_thymeleaf_security.entity.dtos.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.form.Form;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.service.user.form.FormService;
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
    private final FormService formService;

    @GetMapping()
    public String dashboard() {
        return "manager/index";
    }

    @GetMapping("/users-list")
    public String usersListPage(
        Model model,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size
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
            return "admin/users";
        }


    @GetMapping("/edit")
    public String editRedirect(){
        UserEntity lastUser = userService.getLastUser();
        return "redirect:/admin/%s/edit".formatted(lastUser.getId().toString());
    }

    @PostMapping("/{userId}/edit")
    public String editPagePost(
                               @ModelAttribute UserDto userInfo, @PathVariable UUID userId){
        UserEntity update = userService.update(userInfo, userId);
        if(update==null){
            return "redirect:/admin/%s/edit".formatted(userId.toString());}
        return "redirect:/admin/users-list";
    }

    @GetMapping("/{userId}/edit")
    public String editPage(Model model,
                           @PathVariable UUID userId){
        model.addAttribute("user", userService.getById(userId));
        return "admin/edit";
    }

    @GetMapping("/{userId}/delete")
    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    public String deleteUser(@PathVariable UUID userId){
        userService.deleteById(userId);
        return "redirect:/admin/users-list";
    }

    @GetMapping("/add")
    public String addUserPage(){
        return "admin/add";
    }

    @PostMapping("/add")
    public String addUser(
                          @ModelAttribute UserRequestDto userRequestDto,
                          Principal principal){

        UserEntity user = userService.saveByAdmin(userRequestDto, principal.getName());
        if (user==null){
            return "redirect:/admin/add";
        }else{
        return "redirect:/admin/users-list";}
    }

    @GetMapping("/forms")
    public String getFormsPage(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Form> forms = formService.getAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("forms", forms);
        int totalPages = forms.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/User-form/forms";
    }

   @GetMapping("/form")
   public String getFormPage(
           Model model,
           @RequestParam UUID formId
   ){
        model.addAttribute("form", formService.viewFormById(formId));
        return "admin/User-form/form";
   }

   @GetMapping("/form/delete")
   public String deleteForm(
           @RequestParam UUID formId
   ){
        formService.deleteById(formId);
        return "redirect:/forms";
   }
}
