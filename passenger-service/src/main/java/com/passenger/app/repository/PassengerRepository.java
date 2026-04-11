package com.passenger.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import com.passenger.app.entity.PassengerInfo;

public interface PassengerRepository extends JpaRepository<PassengerInfo,Long>{
      List<PassengerInfo> findByBookingId(String bookingId);
      
      Optional<PassengerInfo> findByPassengerId(Long passengerid);
      
      Optional<PassengerInfo> findByPassportNumber(String passportNumber);
      
      Optional<PassengerInfo> findByTicketNumber(String ticketNumber);
      
      Optional<PassengerInfo> findBySeatId(Long seatId);
      
      @Query("select COUNT(p) from PassengerInfo p where p.bookingId=:bookingId")
      Integer countByBookingId(@Param("bookingId")String bookingId);
      
      void deleteByBookingId(String id);
      
}
