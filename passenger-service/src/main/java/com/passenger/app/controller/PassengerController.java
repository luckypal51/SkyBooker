package com.passenger.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.passenger.app.dto.PassengerDto;
import com.passenger.app.dto.RequestDto;
import com.passenger.app.service.PassengerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/passenger")
@Tag(name = "Passenger Service")
public class PassengerController {
	
	@Autowired
	PassengerService passengerService;

	@PostMapping("/add")
	@Operation(summary = "Add Passenger")
	public ResponseEntity<PassengerDto> addPassenger(@Valid @RequestBody PassengerDto passengerDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.addPassenger(passengerDto));
	}
	
	@PostMapping("/add-all")
	@Operation(summary = "Add Multiple Passengers")
	public ResponseEntity<PassengerDto[]> addAllPassenger( @RequestBody PassengerDto[] passengerDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.addAllPassenger(passengerDto));
	}
	@GetMapping("/get-passenger-by-id")
	@Operation(summary = "Get Passenger By Id")
	public ResponseEntity<PassengerDto> getPassengerById(@RequestParam Long passengerId){
		return ResponseEntity.status(HttpStatus.FOUND).body(passengerService.getPassengerById(passengerId).get());
	}
	
	@GetMapping("/get-passengers-by-booking")
	@Operation(summary = "Get Passenger By Booking Id")
	public ResponseEntity<List<PassengerDto>> getPassengerByBooking(@RequestParam Long bookingId){
		return ResponseEntity.status(HttpStatus.OK).body(passengerService.getPassengersByBooking(bookingId));
	}
	
	@GetMapping("/get-passenger-by-passport")
	@Operation(summary = "Get Passenger By Passport")
	public ResponseEntity<PassengerDto> getPassengerByPassportNumber(@RequestParam String passportNumber){
		return ResponseEntity.status(HttpStatus.FOUND).body(passengerService.getByPassportNumber(passportNumber).get());
	}
	
	@PutMapping("/update-passenger")
	@Operation(summary = "Update Passenger By Passenger Id")
	public ResponseEntity<Void> updatePassengerInfo(@RequestParam Long passengerId,@Valid @RequestBody PassengerDto passengerDto){
		passengerService.updatePassenger(passengerId, passengerDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/assign-seat")
	@Operation(summary = "Assign seat to passenger")
	public ResponseEntity<Void> assignSeatToPassenger(@RequestBody RequestDto requestDto){
		passengerService.assignSeat(requestDto.getPassengerId(), requestDto.getSeatId(), requestDto.getSeatNumber());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@GetMapping("/count-seat-by-booking")
	public ResponseEntity<Integer> getCountSeatByBooking(@RequestParam Long bookingId){
		return ResponseEntity.status(HttpStatus.OK).body(passengerService.getPassengerCount(bookingId));
	}
}
