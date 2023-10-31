package com.example.hotel_thymeleaf_security.entity.hotel;


import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "hotels")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HotelEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String city;

    @ManyToMany
    @JoinTable(
            name = "hotel_room_type",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "room_type_id")
    )
    private List<RoomType> roomTypes;

    @ManyToMany
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "room_amenties_id")
    )
    private List<RoomAmenity> roomAmenities;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelFilesEntity> photos;
    @Column(length = 1000000 )
    private String description;
    private double priceRangeMin;
    private double priceRangeMax;

    @Column(nullable = false)
    private boolean availability=false;

    private Boolean cancellationPolicy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Review> reviews;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "map_location_id")
    private MapLocation mapLocation;

    @ManyToMany
    @JoinTable(name = "hotel_payment_methods",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private List<PaymentMethod> paymentOptions;

    @ManyToMany
    @JoinTable(name = "special_offers",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "special_offers_id")
    )
    private List<SpecialOffer> specialOffers;

    @ManyToMany
    @JoinTable(
            name = "hotel_language",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<LanguageSpoken> languageSpokens;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "hotel_events_conferences",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "event_conference_id")
    )
    private List<EventsAndConferencesEntity> eventsAndConferences;

    private boolean petFriendly=false;
    private boolean parkingAvailable=false;

    @JsonIgnore
    private UUID managerOfHotel;
}
