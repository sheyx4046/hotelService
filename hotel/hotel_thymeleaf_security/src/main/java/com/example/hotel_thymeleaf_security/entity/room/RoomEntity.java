package com.example.hotel_thymeleaf_security.entity.room;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomEntity extends BaseEntity {
    @Column(nullable = false,unique = true)
    private String number;
    @Column(nullable = false)
    private String bed;
    private Boolean hasTV;
    private Double price;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    @ManyToOne
    private HotelEntity hotel;



}
