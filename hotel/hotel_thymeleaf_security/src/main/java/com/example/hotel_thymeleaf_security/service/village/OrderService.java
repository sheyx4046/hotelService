package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.booking.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.OrderDto;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.NoPermissionException;
import java.util.UUID;

public interface OrderService extends BaseService<OrderEntity, OrderDto> {
    void delete(UUID OrderId, String userEmail) throws NoPermissionException;
    Page<OrderEntity> getAllPage(Pageable pageable);
    Page<OrderEntity> getByOrderedPage(Pageable pageable, String user);
}
