package com.example.hotel_thymeleaf_security.entity.dtos.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HotelRequestDto {
//    step-1 -basic
    private String name, roomTypes, roomAmenities, city, description,
//    step-2 -details
            specialOffers, languageSpokens, paymentOptions, eventsAndConferences;
    private Boolean availability=false,cancellationPolicy=false,petFriendly=false, parkingAvailable=false,
            creditCard=false,debitCards=false,digitalWallets=false,bankTransfers=false;
    private double priceRangeMin, priceRangeMax;
//    step-3 -social
    private String  phoneNumber,  email, twitter, facebook, youtube, telegram, instagram, googleMap;
    private String managerOfHotel;
}
