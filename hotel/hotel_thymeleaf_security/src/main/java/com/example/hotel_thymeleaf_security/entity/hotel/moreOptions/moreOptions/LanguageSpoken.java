package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;

import java.util.List;

@Entity(name = "language_spoken")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LanguageSpoken extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String language;
    @JsonIgnore
    @ManyToMany(mappedBy = "languageSpokens")
    private List<HotelEntity> hotel;
}
