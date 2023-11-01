package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.dtos.VillageResponseDto;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.ContactInfo;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.FileEntity;
import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.RoomAmenity;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.ContactInfoRepository;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.RoomAmenityRepository;
import com.example.hotel_thymeleaf_security.repository.userRepository.UserRepository;
import com.example.hotel_thymeleaf_security.repository.villa.VillaRepository;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VillaServiceImpl implements VillageService {
    private final VillaRepository villaRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ContactInfoRepository contactInfoRepository;
    private final RoomAmenityRepository roomAmenityRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private List<VillaRentEntity> findByCity(String city ){
        List<VillaRentEntity> villas = villaRepository.findByCity(city).orElseThrow(()->new DataNotFoundException("our villas are not available in the city you entered, please enter another city"));
      return villas;
    }
    private List<VillaRentEntity>findByCityAndDate(String city, LocalDate startDate,LocalDate endDate){
        List<LocalDate>bookingDays = orderService.getDatesBetween(startDate,endDate);
        List<VillaRentEntity> byCity = findByCity(city);
        List<VillaRentEntity>byDate  =new ArrayList();
        for (VillaRentEntity villa : byCity) {
            List<LocalDate> openDays = orderService.DaysOff(villa.getId());
            List<LocalDate> commonDates = orderService.findCommonDates(bookingDays, openDays);
            if(commonDates.isEmpty()){
                byDate.add(villa);
            }
        }
        if(byDate.isEmpty()){
            throw new DataNotFoundException("My main villas are not available in the city you entered on the date you entered. Please enter another city or time");
        }
        return byDate;
    }

    @Override
    public VillaRentEntity create(VillageResponseDto villageResponseDto) {
        VillaRentEntity villaRent = modelMapper.map(villageResponseDto, VillaRentEntity.class);
        return villaRepository.save(villaRent);
    }

    @Override
    public VillaRentEntity getById(UUID id) {
        return villaRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Village with id not found"));
    }

    @Override
    public VillaRentEntity update(VillageResponseDto villageResponseDto, UUID id) {
        return null;
//        TODO update
    }

    @Override
    public void deleteById(UUID id) {
        villaRepository.deleteById(id);
    }

    @Override
    public VillaRentEntity save(VillageResponseDto dto, String name) {
        UserEntity userEntity = userService.getByEmail(name);
        if(checkOfVillage(dto, name)){
            VillaRentEntity villaRent = modelMapper.map(dto, VillaRentEntity.class);
            villaRent.setOwnerId(userEntity.getId());
            villaRent.setContactInfo(
                    getAndSaveContactInfo(dto.getEmail(),dto.getInstagram(),
                            dto.getFacebook(), dto.getPhoneNumber(), dto.getTelegram(),
                            dto.getYoutube(), dto.getGoogleMap()));
            villaRent.setRoomAmenities(
                    getAndSaveRA(dto.getRoomAmenities())
            );
            villaRent.setImages(
                    saveImage(dto.getGeneralImage(), dto.getOtherImage())
            );
        }
        return null;
    }

    private List<FileEntity> saveImage(MultipartFile generalImage, MultipartFile otherImage) {
        return null; //TODO save Image
    }

    private List<RoomAmenity> getAndSaveRA(String roomAmenities) {
        List<RoomAmenity> list = new ArrayList<>();
        for (String amenity : roomAmenities.split(", |,")) {
            Optional<RoomAmenity> roomAmenity = roomAmenityRepository.findRoomAmenitiesByAmenity(amenity);
            if(roomAmenity.isEmpty()){
                RoomAmenity roomAmenity1 = new RoomAmenity();
                roomAmenity1.setAmenity(amenity);
                list.add(roomAmenityRepository.save(roomAmenity1));
            }
            list.add(roomAmenity.orElseThrow(() -> new DataNotFoundException("Could not add")));
        }
        return list;
    }

    private ContactInfo getAndSaveContactInfo(String email, String instagram, String facebook, String phoneNumber, String telegram, String youtube, String googleMap) {
        ContactInfo contactInfo = new ContactInfo(phoneNumber, email, instagram, facebook, youtube, telegram,googleMap);
        return contactInfoRepository.save(contactInfo);
    }

    private Boolean checkOfVillage(VillageResponseDto dto, String ownerEmail){
        UserEntity userEntity = userService.getByEmail(ownerEmail);
        return villaRepository.findByNameAndOwnerId(dto.getName(), userEntity.getId()).isEmpty();
    }
}
