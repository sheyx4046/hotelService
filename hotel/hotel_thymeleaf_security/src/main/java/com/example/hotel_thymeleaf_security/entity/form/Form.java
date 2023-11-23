package com.example.hotel_thymeleaf_security.entity.form;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Entity(name = "form")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Form extends BaseEntity {
    private String userName, email, subject, message;
}
