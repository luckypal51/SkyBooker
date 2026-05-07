package com.booking.app.interservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.app.dto.FlightDto;

@FeignClient(name = "SKYBOOKER")
public interface FlightClient {
	
	 @GetMapping("/flight/service/search-flight-by-id")
	 public FlightDto getFlightById(@RequestParam Long id);
}
