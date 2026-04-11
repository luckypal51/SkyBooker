package com.booking.app.interservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.app.dto.FlightDto;

@FeignClient(name = "SKYBOOKER")
public interface FlightClient {
	
	 @PostMapping("/flight/service/search-flight-by-id")
	 public FlightDto getFlightById(@RequestParam Long id);
}
