package com.booking.app.interservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.app.entity.Seat;

@FeignClient(name = "SEATS")
public interface SeatClient {
	
	@GetMapping("/seats/search-id")
	public Seat getSeatById(@RequestParam Long seatId);

	@GetMapping("/seats/hold-seat")
	public String holdSeat(@RequestParam Long seatId);
	
	@GetMapping("seats/release-seat")
    public String releaseSeat(@RequestParam Long seatId);
}
