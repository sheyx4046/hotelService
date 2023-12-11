package com.example.hotel_thymeleaf_security.service.user.form;

import com.example.hotel_thymeleaf_security.entity.dtos.FormDto;
import com.example.hotel_thymeleaf_security.entity.form.Form;
import com.example.hotel_thymeleaf_security.entity.form.FormStatus;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.form.FormRepository;
import com.example.hotel_thymeleaf_security.service.mailService.MailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService{
    private final ModelMapper modelMapper;
    private final FormRepository formRepository;
    @Override
    public Form create(FormDto formDto) {
        Form map = modelMapper.map(formDto, Form.class);
        map.setFormStatus(FormStatus.NOT_READIED);
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

    @Override
    public Page<Form> getAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        List<Form> all = formRepository.findAllByOrderByFormStatusDescCreatedDateDesc();

        int startItem = currentPage * pageSize;
        List<Form> list;

        if (startItem >= all.size()) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, all.size());
            list = all.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, all.size());
    }

    @Override
    public Form viewFormById(UUID formId) {
        Form byId = getById(formId);
        byId.setFormStatus(FormStatus.READIED);
        formRepository.save(byId);
        return getById(formId);
    }
}
