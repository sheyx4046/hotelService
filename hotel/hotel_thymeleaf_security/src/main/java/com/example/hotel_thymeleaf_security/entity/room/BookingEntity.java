package com.example.hotel_thymeleaf_security.entity.room;


import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingEntity extends BaseEntity {
    private LocalDate localDate;
    @ManyToOne
    private RoomEntity room;
    @OneToOne
    private UserEntity user;

}
