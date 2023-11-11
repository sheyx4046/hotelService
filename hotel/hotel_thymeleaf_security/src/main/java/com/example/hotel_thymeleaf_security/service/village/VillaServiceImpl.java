package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.dtos.VillageResponseDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.ContactInfo;
import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.FileEntity;
import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.PaymentMethod;
import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.RoomAmenity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.UserAccessException;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.ContactInfoRepository;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.PaymentMethodRepository;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.RoomAmenityRepository;
import com.example.hotel_thymeleaf_security.repository.userRepository.UserRepository;
import com.example.hotel_thymeleaf_security.repository.villa.VillaRepository;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VillaServiceImpl implements VillageService {
    private final VillaRepository villaRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ContactInfoRepository contactInfoRepository;
    private final RoomAmenityRepository roomAmenityRepository;
    private final FileService fileService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final PaymentMethodRepository methodRepository;


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
        VillaRentEntity byId = villaRepository.findById(id).orElseThrow(()-> new DataNotFoundException("villa not found"));
        return null;
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
            villaRent.setPaymentOptions(
                    savePaymentMethods(dto.isCash(), dto.isCreditCard())
            );
//            villaRent.setImages(
//                    saveImage(dto.getGeneralImage(), dto.getOtherImage())
//            );
            return villaRepository.save(villaRent);
        }
        return null;
    }

    @Override
    public Page<VillaRentEntity> getAllPage(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        List<VillaRentEntity> all = villaRepository.findAll();

        int startItem = currentPage * pageSize;
        List<VillaRentEntity> list;

        if (startItem >= all.size()) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, all.size());
            list = all.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, all.size());
    }

    @Override
    public Page<VillaRentEntity> getVillageByOwnerEmail(Pageable pageable, String email) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        UUID owner = userService.getByEmail(email).getId();
        if(!email.isEmpty()){
            List<VillaRentEntity> all = villaRepository.findVillaRentEntitiesByOwnerIdOrderByCreatedDate(owner);

            int startItem = currentPage * pageSize;
            List<VillaRentEntity> list;

            if (startItem >= all.size()) {
                list = Collections.emptyList();
            } else {
                int toIndex = Math.min(startItem + pageSize, all.size());
                list = all.subList(startItem, toIndex);
            }

            return new PageImpl<>(list, pageable, all.size());}
        return null;
    }

    @Override
    public void deleteByIdAndUser(UUID villaId, String deleter) {
        UserEntity userEntity = userService.getByEmail(deleter);
        VillaRentEntity byId = getById(villaId);
        switch (userEntity.getRole()){
            case SUPER_ADMIN -> {
                deleteById(villaId);
            }
            case MANAGER -> {
                if(villaRepository.findByNameAndOwnerId(byId.getName(), userEntity.getId()).isPresent()){
                    deleteById(villaId);
                }
                else {
                    throw new UserAccessException("You cannot deleted");
                }
            }
            default -> {
                throw new UserAccessException("You cannot deleted");
            }
        }
    }

    private List<PaymentMethod> savePaymentMethods(boolean cash, boolean creditCard) {
        List<PaymentMethod> list = new ArrayList<>();
        if(cash){
            Optional<PaymentMethod> cash1 = methodRepository.findPaymentMethodByName("Cash");
            if (cash1.isEmpty()){
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setName("Cash");
                list.add(methodRepository.save(paymentMethod));
            }else {
                list.add(cash1.orElseThrow(()-> new DataNotFoundException("Cash could not add")));
            }
        }
        if(creditCard){
            Optional<PaymentMethod> creditCard1 = methodRepository.findPaymentMethodByName("Credit Card");
            if (creditCard1.isEmpty()){
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setName("Credit Card");
                list.add(methodRepository.save(paymentMethod));
            }else {
                list.add(creditCard1.orElseThrow(()-> new DataNotFoundException("Credit Card could not add")));
            }
        }
        return list;
    }

    private List<FileEntity> saveImage(MultipartFile generalImage, MultipartFile otherImage) {
        List<FileEntity> images = new ArrayList<>();
        images.add(fileService.create(generalImage));
        images.add(fileService.create(otherImage));
        return images;
    }

    private List<RoomAmenity> getAndSaveRA(String roomAmenities) {
        List<RoomAmenity> list = new ArrayList<>();
        for (String amenity : roomAmenities.split(", |,")) {
            RoomAmenity roomAmenity = roomAmenityRepository.findRoomAmenitiesByAmenity(amenity);
            if(roomAmenity==null){
                RoomAmenity roomAmenity1 = new RoomAmenity();
                roomAmenity1.setAmenity(amenity);
                list.add(roomAmenityRepository.save(roomAmenity1));
            }
            list.add(roomAmenity);
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
