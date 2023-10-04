package com.example.hotel_thymeleaf_security.entity.hotel;


import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity(name = "hotel")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HotelEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, unique = true)
    private String website;
    @ManyToOne
    private UserEntity owner;
}
