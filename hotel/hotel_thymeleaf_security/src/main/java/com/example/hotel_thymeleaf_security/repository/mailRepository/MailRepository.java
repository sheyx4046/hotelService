package com.example.hotel_thymeleaf_security.repository.mailRepository;

import com.example.hotel_thymeleaf_security.entity.mail.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailRepository extends JpaRepository<MailEntity, UUID> {
}
