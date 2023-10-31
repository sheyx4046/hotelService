package com.example.hotel_thymeleaf_security.service.hotel;

import com.example.hotel_thymeleaf_security.entity.dtos.request.HotelRequestDto;
import com.example.hotel_thymeleaf_security.entity.hotel.HotelEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;

import java.util.List;

public interface HotelService extends BaseService<HotelEntity, HotelRequestDto> {

    List<HotelEntity> getAll (int page, int size);
    List<HotelEntity> findByName(String hotelName);
}
