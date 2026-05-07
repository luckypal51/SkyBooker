package com.app.seats.controller;

import com.app.seats.dto.SeatDto;
import com.app.seats.service.SeatServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/staff/seat")
@Tag(name = "Seat Service For Staff")
public class StaffController {
    @Autowired
    SeatServiceImpl seatService;

    @PostMapping("/add")
    @Operation(summary = "Add the multiple seats")
    public ResponseEntity<String> addSeats( @RequestBody SeatDto[] seatDto){
        seatService.addSeatsForFlight(seatDto[0].getFlightId(), Arrays.asList(seatDto));
        return ResponseEntity.status(HttpStatus.CREATED).body("SuccessFully added "+seatDto.length+" seats");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Seat By Flight Id")
    public ResponseEntity<String> deleteSeats(@RequestParam Long id){
        seatService.deleteSeatsForFlight(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Seats Deleted From Flight Id "+id);
    }
}
