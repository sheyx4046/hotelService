package com.example.hotel_thymeleaf_security.service.village;


import com.example.hotel_thymeleaf_security.entity.bookin.OrderEntity;
import com.example.hotel_thymeleaf_security.entity.dtos.BookingDto;
import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.OrdersException;
import com.example.hotel_thymeleaf_security.repository.booking.OrderRepository;
import com.example.hotel_thymeleaf_security.repository.villa.VillaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import static com.example.hotel_thymeleaf_security.entity.bookin.BookingStatus.BOOKED;
import static com.example.hotel_thymeleaf_security.entity.bookin.BookingStatus.FINISHED;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final VillaRepository villaRepository;
//    @Value("${services.room-url}")

public List<OrderEntity>UserBookingsHistory(Pageable pageable, UUID userId){

return orderRepository.findAllByUserId(userId,pageable).getContent();
}

    public List<LocalDate> DaysOff(UUID villaId) {
        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByBookingStatusRoomId(villaId).orElseThrow(() -> new DataNotFoundException("Booking not found"));
        LocalDate today = LocalDate.now();
        List<LocalDate> thirtyDays = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            thirtyDays.add(today.plusDays(i));
        }

        List<LocalDate> occupiedDays = new ArrayList();

        for (OrderEntity order : orderEntities) {
            LocalDate startDay = order.getStartDay();
            LocalDate endDay = order.getEndDay();

            for (LocalDate day : thirtyDays) {
                if (!day.isBefore(startDay) && !day.isAfter(endDay)) {
                    occupiedDays.add(day);
                }
            }
        }

        List<LocalDate> vacantDays = new ArrayList(thirtyDays);
        vacantDays.removeAll(occupiedDays);

        return vacantDays;
    }
    public  List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }
    public  List<LocalDate> findCommonDates(List<LocalDate> list1, List<LocalDate> list2) {
        List<LocalDate> commonDates = new ArrayList<>();


        for (LocalDate date : list1) {
            if (!list2.contains(date)) {
                commonDates.add(date);
            }
        }

        return commonDates;
    }


    public OrderEntity addOrder(BookingDto bookingDto){
  List<LocalDate>bookingDays = getDatesBetween(bookingDto.startDay,bookingDto.endDay);
  List<LocalDate>openDayRooms = DaysOff(bookingDto.villaId);
  List<LocalDate>commonDates = findCommonDates(bookingDays,openDayRooms);
  if (commonDates.isEmpty()) {
      OrderEntity map = modelMapper.map(bookingDto, OrderEntity.class);
      map.setBookingStatus(BOOKED);
      OrderEntity save = orderRepository.save(map);
      return save;
  }
  throw new OrdersException("Try again in the villa you have already booked or choose another villa");
 }
 public String deleteOrder(UUID orderId, UUID userId){
     OrderEntity byId = orderRepository.findById(orderId).orElseThrow(
             ()->new DataNotFoundException("order not found"));
     if (byId.getUserId() == userId){
         new NoPermissionException("you are not allowed...");
         return null;
     }
     else orderRepository.delete(byId);
     return "order delete";
 }
    @Scheduled(fixedRate = 86400000)
    public void statusUpdate(){
      List<OrderEntity> bookedOrders = orderRepository.findByBookingStatus(BOOKED);
        for (OrderEntity orders:bookedOrders
             ) {
            if (LocalDate.now().isAfter(orders.getEndDay())) {
                orders.setBookingStatus(FINISHED);
            }
        }
    }


    public List<VillaRentEntity> getAll() {
    return villaRepository.findAll();
    }

    public List<VillaRentEntity> findByCityAndDate(String city, LocalDate starDate, LocalDate endDate) {
    List<LocalDate> datesBooking = getDatesBetween(starDate,endDate);
        List<VillaRentEntity> byCity = villaRepository.findByCity(city).orElseThrow(()->new DataNotFoundException("villas no this city"));
        List<VillaRentEntity>villaRentEntities = new ArrayList<>();
        for (VillaRentEntity villa: byCity) {
            if (findCommonDates(datesBooking,DaysOff(villa.getId())).isEmpty())
                villaRentEntities.add(villa);

        }
        return villaRentEntities;
    }

}
