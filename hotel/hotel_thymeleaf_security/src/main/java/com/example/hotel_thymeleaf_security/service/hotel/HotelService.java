package com.example.hotel_thymeleaf_security.service.hotel;


import com.example.hotel_thymeleaf_security.dto.HotelRequestDto;
import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.UniqueObjectException;
import com.example.hotel_thymeleaf_security.repository.HotelRepository;
import com.example.hotel_thymeleaf_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public HotelEntity save(HotelRequestDto hotelRequestDto, Long ownerId) {
        UserEntity userEntity = userRepository.findUserEntityById(ownerId).orElseThrow(() -> new DataNotFoundException("Your access has expired"));
        Optional<HotelEntity> hotelEntityByName = hotelRepository.findHotelEntityByName(hotelRequestDto.getName());
        Optional<HotelEntity> hotelEntityByWebsite = hotelRepository.findHotelEntityByWebsite(hotelRequestDto.getWebsite());
        if (hotelEntityByName.isPresent()) {
            throw new UniqueObjectException("Hotel name already exists");
        } else if (hotelEntityByWebsite.isPresent()) {
            throw new UniqueObjectException("Hotel website already exists");
        }
        HotelEntity hotel = modelMapper.map(hotelRequestDto, HotelEntity.class);
        userEntity.setRoles(Role.ADMIN);
        UserEntity owner = userRepository.save(userEntity);
        hotel.setOwner(owner);
        return hotelRepository.save(hotel);
    }
        public List<HotelEntity> getAll (int page, int size){
            Pageable pageable = PageRequest.of(page, size);
            return hotelRepository.findAll(pageable).getContent();
        }

        public HotelEntity update (HotelRequestDto update,Long id){
            HotelEntity hotel = hotelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("hotel not found"));
            if (update.getName() != null) {
                if(!update.getName().equals(hotel.getName())){
                    Optional<HotelEntity> hotelEntityByName = hotelRepository.findHotelEntityByName(update.getName());
                    if(hotelEntityByName.isPresent()){
                        throw new UniqueObjectException("Hotel name already exists");
                    }
                    hotel.setName(update.getName());
                }
            }
            if(update.getWebsite() != null) {
                if(!update.getWebsite().equals(hotel.getWebsite())){
                    Optional<HotelEntity> hotelEntityByWebsite = hotelRepository.findHotelEntityByWebsite(update.getWebsite());
                    if(hotelEntityByWebsite.isPresent()){
                        throw new UniqueObjectException("Hotel website already exists");
                    }
                    hotel.setWebsite(update.getWebsite());
                }
            }
            if(update.getAddress() != null){
                hotel.setAddress(update.getAddress());
            }
            return hotelRepository.save(hotel);
        }
        public void deleteById (Long id){
            hotelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("This hotel not found"));
            hotelRepository.deleteById(id);
        }
    }
