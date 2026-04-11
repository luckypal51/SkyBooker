package com.booking.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.app.dto.BookingDto;
import com.booking.app.dto.RequestDto;
import com.booking.app.dto.RequestStatus;
import com.booking.app.service.BookingServiceImp;

@Controller
@RequestMapping("/booking")
public class BookingController {
	
	@Autowired
	BookingServiceImp bookingService;
	
	@PostMapping("/create")
	public ResponseEntity<BookingDto> create(@RequestBody RequestDto requestDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(requestDto.getBooking(),requestDto.getPassenger())); 
	}
	
	@DeleteMapping("/cancel")
	public ResponseEntity<Void> deletebooking(@RequestParam String bookingId){
		bookingService.cancelBooking(bookingId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@GetMapping("/view-booking-userId")
	public ResponseEntity<List<BookingDto>> viewAllBookingByUserId(@RequestParam Long userId){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getBookingByUser(userId));
	}
	
	@GetMapping("/view-booking-flightId")
	public ResponseEntity<List<BookingDto>> viewBookingByFlight(@RequestParam Long flightId){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getBookingByFlight(flightId));
	}
	
	@GetMapping("/view-booking-bookingId")
	public ResponseEntity<BookingDto> findBookingById(@RequestParam String bookingId){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getBookingById(bookingId));
	}
	
	@GetMapping("/view-booking-pnr")
	public ResponseEntity<BookingDto> findBookingByPnr(@RequestParam String pnrCode){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getBookingByPnr(pnrCode));
	}
	
	@GetMapping("/view-booking-upcoming")
	public ResponseEntity<List<BookingDto>> findUpComingBooking(@RequestParam Long userId){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getUpComingBookings(userId));
	}
	@PatchMapping("/update-status")
	public ResponseEntity<Void> updateBookingStatus(@RequestBody RequestStatus requestStatus){
		bookingService.updateStatus(requestStatus.getBookingId(),requestStatus.getStatus());
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
