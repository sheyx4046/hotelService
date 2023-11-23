package com.example.hotel_thymeleaf_security.repository.form;

import com.example.hotel_thymeleaf_security.entity.form.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface FormRepository extends JpaRepository<Form, UUID> {
}
