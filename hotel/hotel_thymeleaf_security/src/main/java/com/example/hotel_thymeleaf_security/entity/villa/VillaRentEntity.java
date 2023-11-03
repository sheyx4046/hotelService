package com.example.hotel_thymeleaf_security.entity.villa;
import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToMany
    @JoinTable(name = "images_of_village",
                joinColumns = @JoinColumn(name = "village_Id"),
                inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<FileEntity> images;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @JsonIgnore
    private UUID ownerId;
}
