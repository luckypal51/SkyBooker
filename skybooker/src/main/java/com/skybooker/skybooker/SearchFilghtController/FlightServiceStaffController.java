package com.skybooker.skybooker.SearchFilghtController;

import com.skybooker.skybooker.dto.FlightDto;

import com.skybooker.skybooker.dto.RequestFlightDto;
import com.skybooker.skybooker.service.FlightServiceImp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff/flight")
@Tag(name = "Flight service for staff")
public class FlightServiceStaffController {
    @Autowired
    FlightServiceImp flightServiceImp;

    @PostMapping("/add") 
    @Operation(summary = "Add Flight")
    public void save(@Valid @RequestBody FlightDto flightDto){
       flightServiceImp.addFlight(flightDto);
    }

    @PutMapping("/update-flight")
    @Operation(summary = "Update Flight")
    public void updateFlight(@RequestParam Long id, @Valid @RequestBody FlightDto flightDto){
        flightServiceImp.updateFlight(id,flightDto);
    }

    @PatchMapping("/update-status")
    @Operation(summary = "Update Status Of Flight")
    public void updateStatus(@RequestParam Long id, @RequestParam String status) {
        flightServiceImp.updateStatus(id, status);
    }

    @PostMapping("/decrement-seats")
    @Operation(summary = "decrease seats")
    public void decrementSeats(@RequestBody RequestFlightDto flightDto){
        flightServiceImp.decrementSeats(flightDto.getId(),flightDto.getSeats());
    }

    @PostMapping("/increment-seats")
    @Operation(summary = "increase seat")
    public void incrementSeats(@RequestBody RequestFlightDto flightDto){
        flightServiceImp.incrementSeats(flightDto.getId(),flightDto.getSeats());
    }

    @DeleteMapping("/delete-flight")
    @Operation(summary = "Delete Flight By Id")
    public void deleteFlight(@RequestParam Long id){
        flightServiceImp.deleteFlight(id);
    }
    
    @GetMapping("/get-all-flight")
    public ResponseEntity<List<FlightDto>> getAllFlight(){
    	return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.getAllFlight());
    }
}
