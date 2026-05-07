package com.booking.app.interservice;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.app.dto.PassengerDto;

@FeignClient(name = "PASSENGER-SERVICE")
public interface PassengerClient {
	
	@GetMapping("/passenger/get-passengers-by-booking")
     public List<PassengerDto> getPassengerByBookingId(@RequestParam Long bookingId);
     
	@PutMapping("/passenger/update-passenger")
    void updatePassenger(
        @RequestParam("passengerId") Long passengerId,   
        @RequestBody PassengerDto passengerDto         
    );
}
