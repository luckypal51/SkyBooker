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

@RestController
@RequestMapping("/passenger")
public class PassengerController {
	
	@Autowired
	PassengerService passengerService;

	@PostMapping("/add")
	public ResponseEntity<PassengerDto> addPassenger(@RequestBody PassengerDto passengerDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.addPassenger(passengerDto));
	}
	
	@GetMapping("/get-passenger-by-id")
	public ResponseEntity<PassengerDto> getPassengerById(@RequestParam Long passengerId){
		return ResponseEntity.status(HttpStatus.FOUND).body(passengerService.getPassengerById(passengerId).get());
	}
	
	@GetMapping("/get-passengers-by-booking")
	public ResponseEntity<List<PassengerDto>> getPassengerByBooking(@RequestParam String bookingId){
		return ResponseEntity.status(HttpStatus.FOUND).body(passengerService.getPassengersByBooking(bookingId));
	}
	
	@GetMapping("/get-passenger-by-passport")
	public ResponseEntity<PassengerDto> getPassengerByPassportNumber(@RequestParam String passportNumber){
		return ResponseEntity.status(HttpStatus.FOUND).body(passengerService.getByPassportNumber(passportNumber).get());
	}
	
	@PutMapping("/update-passenger")
	public ResponseEntity<PassengerDto> updatePassengerInfo(@RequestParam Long passengerId,@RequestBody PassengerDto passengerDto){
		return ResponseEntity.status(HttpStatus.OK).body(passengerService.updatePassenger(passengerId, passengerDto));
	}
	
	@GetMapping("/assign-seat")
	public ResponseEntity<Void> assignSeatToPassenger(@RequestBody RequestDto requestDto){
		passengerService.assignSeat(requestDto.getPassengerId(), requestDto.getSeatId(), requestDto.getSeatNumber());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@GetMapping("/count-seat-by-booking")
	public ResponseEntity<Integer> getCountSeatByBooking(@RequestParam String bookingId){
		return ResponseEntity.status(HttpStatus.OK).body(passengerService.getPassengerCount(bookingId));
	}
}
