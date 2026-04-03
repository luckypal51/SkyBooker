package com.app.seats.controller;

import com.app.seats.dto.SeatDto;
import com.app.seats.service.SeatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("staff/seat")
public class StaffController {
    @Autowired
    SeatServiceImpl seatService;

    @PostMapping("/add")
    public ResponseEntity<String> addSeats(@RequestBody SeatDto[] seatDto){
        seatService.addSeatsForFlight(seatDto[0].getFlightId(), Arrays.asList(seatDto));
        return ResponseEntity.status(HttpStatus.CREATED).body("SuccessFully added "+seatDto.length+" seats");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSeats(@RequestParam Long id){
        seatService.deleteSeatsForFlight(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Seats Deleted From Flight Id "+id);
    }
}
