package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

import java.util.List;

@Entity(name="special_offer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecialOffer extends BaseEntity {
    private String offer;

    @ManyToMany(mappedBy = "specialOffers")
    private List<HotelEntity> hotelEntityList;
}
