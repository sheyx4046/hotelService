package com.example.hotel_thymeleaf_security.service.hotel;


import com.example.hotel_thymeleaf_security.entity.dtos.request.HotelRequestDto;
import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.*;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.HotelRepository;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.*;
import com.example.hotel_thymeleaf_security.repository.userRepository.UserRepository;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RoomTypesRepository roomTypesRepository;
    private final LanguageSpokenRepository languageSpokenRepository;
    private final RoomAmenityRepository roomAmenityRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final EventAndConferencesRepository eventAndConferencesRepository;
    private final ContactInfoRepository contactInfoRepository;



    public List<HotelEntity> getAll (int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return hotelRepository.findAll(pageable).getContent();
    }

    @Override
    public List<HotelEntity> findByName(String hotelName) {
        return hotelRepository.findByNameContaining(hotelName);
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

    private List<LanguageSpoken> checkAndCreateLanguage(String languages) {
        List<LanguageSpoken> languageSpokenList = new ArrayList<>();
        for (String language : languages.split(", |,")) {
            LanguageSpoken existingLanguage = languageSpokenRepository.findLanguageSpokenByLanguage(language);
            if (existingLanguage == null) {
                LanguageSpoken savedLanguage = new LanguageSpoken();
                savedLanguage.setLanguage(language);
                languageSpokenList.add(languageSpokenRepository.save(savedLanguage));
            } else {
                languageSpokenList.add(existingLanguage);
            }
        }
        return languageSpokenList;
    }

    private List<RoomType> checkRoomTypeAndCreateType(String roomTypes){
        List<RoomType> roomTypeList = new ArrayList<>();
        for (String type : roomTypes.split(", |,")) {
            Optional<RoomType> found = roomTypesRepository.findRoomTypeByTypeName(type);
            if (found.isEmpty()) {
                RoomType rt = new RoomType();
                rt.setTypeName(type);
                roomTypeList.add(roomTypesRepository.save(rt));
            } else {
                roomTypeList.add(found.orElseThrow(()-> new DataNotFoundException("RoomType not found")));
            }
        }
        return roomTypeList;
    }

    private List<RoomAmenity> checkRoomAmenitiesAndSaCreateAmenity(String roomAmenities) {
        List<RoomAmenity> roomAmenitiesList = new ArrayList<>();
        for (String amenity : roomAmenities.split(", |,")) {
            Optional<RoomAmenity> found = roomAmenityRepository.findRoomAmenitiesByAmenity(amenity);
            if (found.isEmpty()) {
                RoomAmenity rm = new RoomAmenity();
                rm.setAmenity(amenity);
                roomAmenitiesList.add(roomAmenityRepository.save(rm));
            } else {
                roomAmenitiesList.add(found.orElseThrow(()-> new DataNotFoundException("Amenity not found")));
            }
        }
        return roomAmenitiesList;
    }


    private List<PaymentMethod> checkPaymentMethodAndCreatePayment(String paymentOptions) {
        List<PaymentMethod> paymentMethodsList = new ArrayList<>();
        for (String i : paymentOptions.split(", |,")) {
            Optional<PaymentMethod> found = paymentMethodRepository.findPaymentMethodByName(i);
            if (found.isEmpty()) {
                PaymentMethod rm = new PaymentMethod();
                rm.setName(i);
                paymentMethodsList.add(paymentMethodRepository.save(rm));
            } else {
                paymentMethodsList.add(found.orElseThrow(()-> new DataNotFoundException("Payment_method not found")));
            }
        }
        return paymentMethodsList;
    }


    private List<EventsAndConferencesEntity> checkEventsAndConferenceToCreate(String eventsAndConferences) {
        List<EventsAndConferencesEntity> eventsAndConferencesEntityList = new ArrayList<>();
        String[] EC = eventsAndConferences.split(", |,");
        for (String eventsAndConferences1 : EC) {
            Optional<EventsAndConferencesEntity> found = eventAndConferencesRepository.findEventsAndConferencesEntitiesByEvent(eventsAndConferences1);
            if (found.isEmpty()) {
                EventsAndConferencesEntity EV = new EventsAndConferencesEntity();
                EV.setEvent(eventsAndConferences1);
                eventsAndConferencesEntityList.add(eventAndConferencesRepository.save(EV));
            } else {
                eventsAndConferencesEntityList.add(found.orElseThrow(()-> new DataNotFoundException("Event not found")));
            }
        }
        return eventsAndConferencesEntityList;
    }

    @Override
    public HotelEntity create(HotelRequestDto hotelRequestDto) {
        HotelEntity hotel = HotelEntity.builder()
                .name(hotelRequestDto.getName())
                .paymentOptions(checkPaymentMethodAndCreatePayment(
                        hotelRequestDto.getPaymentOptions()
                ))
                .eventsAndConferences(checkEventsAndConferenceToCreate(
                        hotelRequestDto.getEventsAndConferences()
                ))
                .roomAmenities(checkRoomAmenitiesAndSaCreateAmenity(
                        hotelRequestDto.getRoomAmenities()
                ))
                .roomTypes(checkRoomTypeAndCreateType(
                        hotelRequestDto.getRoomTypes()
                ))
                .languageSpokens(
                       checkAndCreateLanguage(hotelRequestDto.getLanguageSpokens())
                )
                .contactInfo(new ContactInfo(
                        hotelRequestDto.getPhoneNumber(),
                        hotelRequestDto.getEmail(),
                        hotelRequestDto.getInstagram(),
                        hotelRequestDto.getFacebook(),
                        hotelRequestDto.getYoutube(),
                        hotelRequestDto.getTwitter(),
                        hotelRequestDto.getTelegram(),
                        hotelRequestDto.getGoogleMap()
                ))
                .description(hotelRequestDto.getDescription())
                .cancellationPolicy(hotelRequestDto.getCancellationPolicy())
                .availability(hotelRequestDto.getAvailability())
                .petFriendly(hotelRequestDto.getPetFriendly())
                .parkingAvailable(hotelRequestDto.getParkingAvailable())
                .managerOfHotel(userService.getByEmail(hotelRequestDto.getManagerOfHotel()).getId())
                .priceRangeMax(hotelRequestDto.getPriceRangeMax())
                .priceRangeMin(hotelRequestDto.getPriceRangeMin())
                .build();
        return hotelRepository.save(hotel);
    }

    @Override
    public HotelEntity getById(UUID id) {
        return hotelRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Hotel not found by ID"));
    }

    @Override
    public HotelEntity update(HotelRequestDto hotelRequestDto, UUID id) {
        HotelEntity map = modelMapper.map(hotelRequestDto, HotelEntity.class);
        HotelEntity hotelEntity = hotelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Hotel not found with ID"));
        hotelEntity.setUpdatedDate(LocalDateTime.now());
        return hotelRepository.save(map);
    }

    @Override
    public void deleteById(UUID id) {
        hotelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("This hotel not found"));
        hotelRepository.deleteById(id);
    }

    private ContactInfo saveContact(ContactInfo contact){
        return contactInfoRepository.save(contact);
    }

    private List<PaymentMethod> savePaymentMethods(
            Boolean creditCard, Boolean debitCards, Boolean digitalWallets, Boolean bankTransfers
    ) {
        List<PaymentMethod> savedPaymentMethods = new ArrayList<>();

        if (creditCard) {
            savedPaymentMethods.add(createAndSavePaymentMethod("Credit Card"));
        }

        if (debitCards) {
            savedPaymentMethods.add(createAndSavePaymentMethod("Debit Card"));
        }

        if (digitalWallets) {
            savedPaymentMethods.add(createAndSavePaymentMethod("Digital Wallet"));
        }

        if (bankTransfers) {
            savedPaymentMethods.add(createAndSavePaymentMethod("Bank Transfer"));
        }

        return savedPaymentMethods;
    }

    private PaymentMethod createAndSavePaymentMethod(String methodName) {
        PaymentMethod paymentMethod = new PaymentMethod();
        Optional<PaymentMethod> paymentMethodByName = paymentMethodRepository.findPaymentMethodByName(methodName);
        if(paymentMethodByName.isEmpty()){
            paymentMethod.setName(methodName);
            return paymentMethodRepository.save(paymentMethod);
        }
        return paymentMethodByName.orElseThrow(()-> new DataNotFoundException("Payment method not found"));
    }
}
