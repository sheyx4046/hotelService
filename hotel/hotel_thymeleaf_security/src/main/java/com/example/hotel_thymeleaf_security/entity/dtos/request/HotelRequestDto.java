package com.example.hotel_thymeleaf_security.entity.dtos.request;

import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.*;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.region.City;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HotelRequestDto {
    private String name;
    private List<RoomType> roomTypes;
    private List<RoomAmenity> roomAmenities;
    private List<HotelFilesEntity> photos;
    private String description;
    private double priceRangeMin;
    private double priceRangeMax;
    private boolean availability=false;
    private Boolean cancellationPolicy;
    private ContactInfo contactInfo;
    private MapLocation mapLocation;
    private List<PaymentMethod> paymentOptions;
    private List<SpecialOffer> specialOffers;
    private List<LanguageSpoken> languageSpokens;
    private List<EventsAndConferencesEntity> eventsAndConferences;
    private boolean petFriendly=false;
    private boolean parkingAvailable=false;
    private UUID managerOfHotel;
}
