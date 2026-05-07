package com.passenger.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.passenger.app.service.PassengerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/passenger")
@Tag(name = "Passenger Service For Admin")
public class PassengerAdmin {
	
	@Autowired
	PassengerService passengerService;

	@DeleteMapping("/delete")
	@Operation(summary = "Delete Passenger By Id")
	public ResponseEntity<Void> deletePassenger(@RequestParam Long passengerId){
		passengerService.deletePassenger(passengerId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
