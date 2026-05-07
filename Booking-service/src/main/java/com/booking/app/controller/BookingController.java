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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.app.dto.BookingDto;
import com.booking.app.dto.RequestDto;
import com.booking.app.dto.RequestStatus;
import com.booking.app.service.serviceimpl.BookingServiceImp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
@Tag(name = "Booking Service")
public class BookingController {
	
	@Autowired
	BookingServiceImp bookingService;
	
	@PostMapping("/create")
	@Operation(summary = "Create Booking by BookingDto and Passenger List")
	public ResponseEntity<BookingDto> create(@Valid @RequestBody RequestDto requestDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(requestDto.getBooking(),requestDto.getPassenger())); 
	}
	
	@DeleteMapping("/cancel")
	@Operation(summary = "Cancel Booking By booking Id")
	public ResponseEntity<Void> deletebooking(@RequestParam Long bookingId){
		bookingService.cancelBooking(bookingId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@GetMapping("/confirm")
	@Operation(summary = "Confirm Booking By Booking Id")
	public ResponseEntity<Void> confirmBooking(@RequestParam Long bookingId){
		bookingService.confirmBooking(bookingId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/view-booking-userId")
	@Operation(summary = "View Bookings By UserId")
	public ResponseEntity<List<BookingDto>> viewAllBookingByUserId(@RequestParam Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingByUser(userId));
	}
	
	@GetMapping("/view-booking-flightId")
	@Operation(summary = "View Bookings By FlightId")
	public ResponseEntity<List<BookingDto>> viewBookingByFlight(@RequestParam Long flightId){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getBookingByFlight(flightId));
	}
	
	@GetMapping("/view-booking-bookingId")
	@Operation(summary = "View Bookings By Booking Id")
	public ResponseEntity<BookingDto> findBookingById(@RequestParam Long bookingId){
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingById(bookingId));
	}
	
	@GetMapping("/view-booking-pnr")
	@Operation(summary = "View Booking By PnrCode")
	public ResponseEntity<BookingDto> findBookingByPnr(@RequestParam String pnrCode){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getBookingByPnr(pnrCode));
	}
	
	@GetMapping("/view-booking-upcoming")
	@Operation(summary = "View Upcoming Booking")
	public ResponseEntity<List<BookingDto>> findUpComingBooking(@RequestParam Long userId){
		return ResponseEntity.status(HttpStatus.FOUND).body(bookingService.getUpComingBookings(userId));
	}
	@PutMapping("/update-status")
	@Operation(summary = "Update status of Booking")
	public ResponseEntity<Void> updateBookingStatus(@RequestBody RequestStatus requestStatus){
		bookingService.updateStatus(requestStatus.getBookingId(),requestStatus.getStatus());
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/ticket")
	public ResponseEntity<byte[]> getTicket(@RequestParam Long bookingId,@RequestParam Long flightId){
		return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=ticket.pdf")
	            .header("Content-Type", "application/pdf")
	            .body(bookingService.create(bookingId,flightId));
	}
	
}
