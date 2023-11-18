package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.dtos.VillageResponseDto;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface VillageService extends BaseService<VillaRentEntity, VillageResponseDto> {

    VillaRentEntity save(VillageResponseDto dto, String name);
    VillaRentEntity update(VillageResponseDto villageResponseDto, UUID id, String ownerEmail);
    Page<VillaRentEntity> getAllPage(Pageable pageable);
    Page<VillaRentEntity> getVillageByOwnerEmail(Pageable pageable, String email);
    void deleteByIdAndUser(UUID villaId, String deleter);
    List<VillaRentEntity> getAllByOwner(String owner);
    List<String> getPaymentMethods(UUID villageID);
    VillaRentEntity getLastVillage(String name);
}
