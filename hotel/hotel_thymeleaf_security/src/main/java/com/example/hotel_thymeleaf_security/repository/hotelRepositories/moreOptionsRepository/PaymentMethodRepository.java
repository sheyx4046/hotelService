package com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository;

import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    Optional<PaymentMethod> findPaymentMethodByName(String name);
}
