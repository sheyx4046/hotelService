package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

import java.util.List;

@Entity(name = "payment_method")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentMethod extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "paymentOptions")
    private List<VillaRentEntity> hotel;
}
