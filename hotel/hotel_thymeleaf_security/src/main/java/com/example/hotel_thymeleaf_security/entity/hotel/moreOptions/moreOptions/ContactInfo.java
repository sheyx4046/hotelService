package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity(name = "contact_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactInfo extends BaseEntity {
    private String phoneNumber=null, email=null, instagram=null,
            facebook=null, youtube=null, telegram=null,
            google_map_location=null;
}
