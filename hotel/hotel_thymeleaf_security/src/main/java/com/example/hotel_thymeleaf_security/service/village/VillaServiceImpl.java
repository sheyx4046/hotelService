package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.dtos.FindDto;
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
import com.example.hotel_thymeleaf_security.repository.villa.VillaRepository;
import com.example.hotel_thymeleaf_security.service.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VillaServiceImpl implements VillageService {
    private final VillaRepository villaRepository;
    private final UserService userService;
    private final ContactInfoRepository contactInfoRepository;
    private final RoomAmenityRepository roomAmenityRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final PaymentMethodRepository methodRepository;
    private final OrderService orderService;

    public VillaServiceImpl(VillaRepository villaRepository, @Lazy UserService userService, ContactInfoRepository contactInfoRepository, RoomAmenityRepository roomAmenityRepository, FileService fileService, ModelMapper modelMapper, PaymentMethodRepository methodRepository,@Lazy OrderService orderService) {
        this.villaRepository = villaRepository;
        this.userService = userService;
        this.contactInfoRepository = contactInfoRepository;
        this.roomAmenityRepository = roomAmenityRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
        this.methodRepository = methodRepository;
        this.orderService = orderService;
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
    }

    @Override
    public VillaRentEntity update(VillageResponseDto villageResponseDto, UUID id, String ownerEmail) {
        UserEntity userEntity = userService.getByEmail(ownerEmail);
        if(checkOfVillage(villageResponseDto, ownerEmail)){
            VillaRentEntity villaRent = getById(id);
            villaRent.setName(villageResponseDto.getName());
            villaRent.setDescription(villageResponseDto.getDescription());
            villaRent.setPrice(villageResponseDto.getPrice());
            villaRent.setRoomAmount(villageResponseDto.getRoomAmount());
            villaRent.setOwnerId(userEntity.getId());
            villaRent.setContactInfo(
                    getAndSaveContactInfo(villageResponseDto.getEmail(),villageResponseDto.getInstagram(),
                            villageResponseDto.getFacebook(), villageResponseDto.getPhoneNumber(), villageResponseDto.getTelegram(),
                            villageResponseDto.getYoutube(), villageResponseDto.getGoogleMap()));
            villaRent.setRoomAmenities(
                    getAndSaveRA(villageResponseDto.getRoomAmenities())
            );
            villaRent.setPaymentOptions(
                    savePaymentMethods(villageResponseDto.isCash(), villageResponseDto.isCreditCard())
            );
            villaRent.setImages(
                    saveImage(villageResponseDto.getGeneralImage(), villageResponseDto.getOtherImage())
            );
            villaRent.setUpdatedDate(LocalDateTime.now());
            return villaRepository.save(villaRent);
        }
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
            villaRent.setImages(
                    saveImage(dto.getGeneralImage(), dto.getOtherImage())
            );
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
        UserEntity userEntity = userService.getByEmail(email);
        switch (userEntity.getRole()){
            case ADMIN, SUPER_ADMIN ->{
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
            case MANAGER -> {
                List<VillaRentEntity> all = villaRepository.findVillaRentEntitiesByOwnerIdOrderByCreatedDate(userEntity.getId());
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
            default -> {
                return null;}
        }
    }

    public String getAmenitiesAsString(UUID hotelId, boolean getAll){
        List<RoomAmenity> villaRentEntity = getById(hotelId).getRoomAmenities();
        if(getAll && !villaRentEntity.isEmpty()) {
            return villaRentEntity.stream().map(RoomAmenity::getAmenity)
                    .collect(Collectors.joining(", "));
        }else
        {
            return "";
        }
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

    @Override
    public List<VillaRentEntity> getAllByOwner(String owner) {
        UserEntity userEntity = userService.getByEmail(owner);
        return villaRepository.findVillaRentEntitiesByOwnerId(userEntity.getId());
    }

    @Override
    public List<String> getPaymentMethods(UUID villageID) {
        List<String> list1 =new ArrayList<>();
        for (PaymentMethod paymentMethod: getById(villageID).getPaymentOptions()) {
            list1.add(paymentMethod.getName());
        }
        return list1;
    }

    @Override
    public VillaRentEntity getLastVillage(String owner) {
        UserEntity userEntity = userService.getByEmail(owner);
        switch (userEntity.getRole()){
            case ADMIN, SUPER_ADMIN -> {
                List<VillaRentEntity> all = villaRepository.findAll();
                return all.get(all.size()-1);
            }
            case MANAGER -> {
                List<VillaRentEntity> allByOwner = getAllByOwner(owner);
                return allByOwner.get(allByOwner.size()-1);
            }
            default -> {
                return null;}
        }
    }

    @Override
    public String getLocation(UUID villageId) {
        VillaRentEntity villaRent = getById(villageId);
        return villaRent.getContactInfo().getGoogle_map_location();
    }

    @Override
    public ContactInfo getContactInfo(UUID villageId) {
        return getById(villageId).getContactInfo();
    }

    @Override
    public Page<VillaRentEntity> searchByDatesAndPricesAndCity(FindDto findDto, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        List<VillaRentEntity> villages = villaRepository.findHotelsBySearchCriteria(findDto.getCity(), findDto.getMinPrice(), findDto.getMaxPrice(), findDto.getFrom(), findDto.getTo());
        int startItem = currentPage*pageSize;
        List<VillaRentEntity> list;
        if (startItem >= villages.size()){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, villages.size());
            list = villages.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, pageable, villages.size());
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
