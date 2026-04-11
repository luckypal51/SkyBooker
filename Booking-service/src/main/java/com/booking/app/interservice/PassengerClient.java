package com.booking.app.interservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.app.dto.PassengerDto;

@FeignClient(name = "PASSENGER-SERVICE")
public interface PassengerClient {
	
	@GetMapping("/passenger/get-passengers-by-booking")
     public List<PassengerDto> getPassengerByBookingId(@RequestParam String bookingId);
     
     @PutMapping("/passenger/update-passenger")
     public void updatePassenger(@RequestBody PassengerDto passengerDto);
}
