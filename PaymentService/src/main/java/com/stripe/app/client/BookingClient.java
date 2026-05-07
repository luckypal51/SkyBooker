package com.stripe.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.stripe.app.dto.RequestStatus;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingClient {
   
	@PutMapping("/booking/update-status")
	public void updateBookingStatus(@RequestBody RequestStatus requestStatus);
	
	@DeleteMapping("/booking/cancel")
	public void deleteBooking(@RequestParam Long bookingId);
	
	@GetMapping("/booking/confirm")
	public void confirmBooking(@RequestParam Long bookingId);
}
