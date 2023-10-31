package com.example.hotel_thymeleaf_security.service;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.villa.VillaRepository;
import com.example.hotel_thymeleaf_security.service.hotel.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VillaService {
    private final VillaRepository villaRepository;
    private final OrderService orderService;
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

}
