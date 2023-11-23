package com.example.hotel_thymeleaf_security.service.user.form;

import com.example.hotel_thymeleaf_security.entity.dtos.FormDto;
import com.example.hotel_thymeleaf_security.entity.form.Form;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.form.FormRepository;
import com.example.hotel_thymeleaf_security.service.mailService.MailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService{
    private final ModelMapper modelMapper;
    private final FormRepository formRepository;
    @Override
    public Form create(FormDto formDto) {
        Form map = modelMapper.map(formDto, Form.class);
        return formRepository.save(map);
    }

    @Override
    public Form getById(UUID id) {
        return formRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Form by Id not found"));
    }

    @Override
    public Form update(FormDto formDto, UUID id) {
        Form form = getById(id);
        form.setSubject(form.getSubject());
        form.setMessage(form.getMessage());
        form.setEmail(form.getEmail());
        form.setUserName(form.getUserName());
        form.setUpdatedDate(LocalDateTime.now());
        return formRepository.save(form);
    }

    @Override
    public void deleteById(UUID id) {
        Form form = getById(id);
        formRepository.deleteById(form.getId());
    }
}
