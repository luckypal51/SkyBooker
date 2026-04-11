package com.passenger.app.feigclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.passenger.app.dto.SeatDto;

@FeignClient(name = "SEATS")
public interface SeatServiceClient {
      
	@GetMapping("/seats/search-id")
	public SeatDto getSeatById(@RequestParam Long seatId);
	
    @GetMapping("/seats/confirm-seat")
    public String confirmSeat(@RequestParam Long seatId);
}
