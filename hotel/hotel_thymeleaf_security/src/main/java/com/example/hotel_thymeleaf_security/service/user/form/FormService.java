package com.example.hotel_thymeleaf_security.service.user.form;

import com.example.hotel_thymeleaf_security.entity.dtos.FormDto;
import com.example.hotel_thymeleaf_security.entity.form.Form;
import com.example.hotel_thymeleaf_security.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FormService extends BaseService<Form, FormDto> {
    Page<Form> getAll(Pageable pageable);

    Form viewFormById(UUID formId);
}
