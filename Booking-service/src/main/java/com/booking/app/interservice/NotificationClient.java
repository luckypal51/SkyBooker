package com.booking.app.interservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.app.dto.BookingConfirmDto;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {
       
	@PostMapping("/notifications/confirm-booking")
	public void confirmBooking(@RequestBody BookingConfirmDto bookingConfirmDto);
}
