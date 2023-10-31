package com.example.hotel_thymeleaf_security.entity.villa;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.ContactInfo;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.PaymentMethod;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.RoomAmenity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "villa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VillaRentEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String city;
    private Double price;
    private int roomAmount;
    @ManyToMany
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "room_amenties_id")
    )
    private List<RoomAmenity> roomAmenities;
    @Column(length = 1000000 )
    private String description;

    @ManyToMany
    @JoinTable(name = "hotel_payment_methods",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private List<PaymentMethod> paymentOptions;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;
    private UUID ownerId;
}
