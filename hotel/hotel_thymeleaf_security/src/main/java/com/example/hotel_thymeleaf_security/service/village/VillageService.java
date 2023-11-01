package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.dtos.VillageResponseDto;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;

public interface VillageService extends BaseService<VillaRentEntity, VillageResponseDto> {

    VillaRentEntity save(VillageResponseDto dto, String name);
}
