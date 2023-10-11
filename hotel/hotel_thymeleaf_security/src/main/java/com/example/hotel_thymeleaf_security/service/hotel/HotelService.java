package com.example.hotel_thymeleaf_security.service.hotel;


import com.example.hotel_thymeleaf_security.entity.dtos.request.HotelRequestDto;
import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.*;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.HotelRepository;
import com.example.hotel_thymeleaf_security.repository.userRepository.UserRepository;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoomTypesRepository roomTypesRepository;
    private final LanguageSpokenRepository languageSpokenRepository;
    private final RoomAmenityRepository roomAmenityRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final EventAndConferencesRepository eventAndConferencesRepository;
    public HotelEntity save(HotelRequestDto hotelRequestDto, Long ownerId) {
        HotelEntity map = modelMapper.map(hotelRequestDto, HotelEntity.class);
        map.setPaymentOptions(checkPaymentMethodAndCreatePayment(hotelRequestDto.getPaymentOptions()));
        map.setLanguageSpokens(checkAndCreateLanguage(hotelRequestDto.getLanguageSpokens()));
        map.setRoomTypes(checkRoomTypeAndCreateType(hotelRequestDto.getRoomTypes()));
        map.setRoomAmenities(checkRoomAmenitiesAndSaCreateAmenity(hotelRequestDto.getRoomAmenities()));
        map.setEventsAndConferences(checkEventsAndConferenceToCreate(hotelRequestDto.getEventsAndConferences()));
        map.setManagerOfHotel(UUID.randomUUID());   //TODO must changed when baseEntity will be LONG-->UUID
        return hotelRepository.save(map);
    }
        public List<HotelEntity> getAll (int page, int size){
            Pageable pageable = PageRequest.of(page, size);
            return hotelRepository.findAll(pageable).getContent();
        }


        public HotelEntity update (HotelRequestDto update,Long id){
            HotelEntity map = modelMapper.map(update, HotelEntity.class);
            HotelEntity hotelEntity = hotelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Hotel not found with ID"));
            hotelEntity.setUpdatedDate(LocalDateTime.now());
            return hotelRepository.save(map);
        }
        public void deleteById (Long id){
            hotelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("This hotel not found"));
            hotelRepository.deleteById(id);
        }
    private static <T> List<T> convertFromStrToClass(List<String> list, Class<T> clazz) {
        List<T> convertedList = new ArrayList<>();

        try {
            for (String str : list) {
                T obj = clazz.getDeclaredConstructor(String.class).newInstance(str);
                convertedList.add(obj);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return convertedList;
    }

    private List<LanguageSpoken> checkAndCreateLanguage(List<LanguageSpoken> languages) {
        List<LanguageSpoken> languageSpokenList = new ArrayList<>();
        for (LanguageSpoken language : languages) {
            LanguageSpoken existingLanguage = languageSpokenRepository.findLanguageSpokenByLanguage(language.getLanguage());
            if (existingLanguage == null) {
                LanguageSpoken savedLanguage = languageSpokenRepository.save(language);
                languageSpokenList.add(savedLanguage);
            } else {
                languageSpokenList.add(existingLanguage);
            }
        }
        return languageSpokenList;
    }

    private List<RoomType> checkRoomTypeAndCreateType(List<RoomType> roomTypes){
        List<RoomType> roomTypeList = new ArrayList<>();
        for (RoomType type : roomTypes) {
            Optional<RoomType> found = roomTypesRepository.findRoomTypeByTypeName(type.getTypeName());
            if (found.isEmpty()) {
                RoomType rt = roomTypesRepository.save(type);
                roomTypeList.add(rt);
            } else {
                roomTypeList.add(found.orElseThrow(()-> new DataNotFoundException("RoomType not found")));
            }
        }
        return roomTypeList;
    }

    private List<RoomAmenity> checkRoomAmenitiesAndSaCreateAmenity(List<RoomAmenity> roomAmenities) {
        List<RoomAmenity> roomAmenitiesList = new ArrayList<>();
        for (RoomAmenity amenity : roomAmenities) {
            Optional<RoomAmenity> found = roomAmenityRepository.findRoomAmenitiesByAmenity(amenity.getAmenity());
            if (found.isEmpty()) {
                RoomAmenity rm = roomAmenityRepository.save(amenity);
                roomAmenitiesList.add(rm);
            } else {
                roomAmenitiesList.add(found.orElseThrow(()-> new DataNotFoundException("Amenity not found")));
            }
        }
        return roomAmenitiesList;
    }


    private List<PaymentMethod> checkPaymentMethodAndCreatePayment(List<PaymentMethod> paymentOptions) {
        List<PaymentMethod> paymentMethodsList = new ArrayList<>();
        for (PaymentMethod amenity : paymentOptions) {
            Optional<PaymentMethod> found = paymentMethodRepository.findPaymentMethodByName(amenity.getName());
            if (found.isEmpty()) {
                PaymentMethod rm = paymentMethodRepository.save(amenity);
                paymentMethodsList.add(rm);
            } else {
                paymentMethodsList.add(found.orElseThrow(()-> new DataNotFoundException("Payment_method not found")));
            }
        }
        return paymentMethodsList;
    }


    private List<EventsAndConferencesEntity> checkEventsAndConferenceToCreate(List<EventsAndConferencesEntity> eventsAndConferences) {
        List<EventsAndConferencesEntity> eventsAndConferencesEntityList = new ArrayList<>();
        for (EventsAndConferencesEntity eventsAndConferences1 : eventsAndConferences) {
            Optional<EventsAndConferencesEntity> found = eventAndConferencesRepository.findEventsAndConferencesEntitiesByEvent(eventsAndConferences1.getEvent());
            if (found.isEmpty()) {
                EventsAndConferencesEntity rm = eventAndConferencesRepository.save(eventsAndConferences1);
                eventsAndConferencesEntityList.add(rm);
            } else {
                eventsAndConferencesEntityList.add(found.orElseThrow(()-> new DataNotFoundException("Event not found")));
            }
        }
        return eventsAndConferencesEntityList;
    }
    }
