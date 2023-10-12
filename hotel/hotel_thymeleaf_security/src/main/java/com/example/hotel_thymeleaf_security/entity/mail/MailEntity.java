package com.example.hotel_thymeleaf_security.entity.mail;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "mail_sends")
@Data
public class MailEntity extends BaseEntity {
    @Column(length = 1000000)
    private String message;
    private String toUser;
}