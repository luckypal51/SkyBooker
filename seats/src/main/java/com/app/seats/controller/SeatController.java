package com.app.seats.controller;

import com.app.seats.dto.RequestCount;
import com.app.seats.dto.RequestDto;
import com.app.seats.dto.RequestSearchDto;
import com.app.seats.dto.SeatDto;
import com.app.seats.service.SeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@Tag(name = "Seat service for user")
public class SeatController {
    @Autowired
    SeatService seatService;

    @GetMapping("/available-seats")
    @Operation(summary = "Seats which has status 'AVAILABLE'")
    public ResponseEntity<List<SeatDto>> getAvailable(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getAvailableSeats(id));
    }

    @GetMapping("/available-class")
    @Operation(summary = "Available seats by class like ECONOMY, BUSSINESS")
    public ResponseEntity<List<SeatDto>> getAvailableSeatByClass(@RequestBody RequestDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getAvailableByClass(dto.getFlightid(), dto.getCls()));
    }

    @GetMapping("/search-id")
    @Operation(summary = "Search Seat By SeatId")
    public ResponseEntity<SeatDto> getById(@RequestParam Long seatId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatById(seatId));
    }

    @GetMapping("/hold-seat")
    @Operation(summary = "HOlD Seat change seat status from AVAILABLE TO HELD")
    public ResponseEntity<String> holdSeat(@RequestParam Long seatId){
        seatService.holdSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body("Held Seat SuccessFully");
    }
    @GetMapping("/release-seat")
    @Operation(summary = "Release Seat change seat status from HELD TO AVAILABLE ")
    public ResponseEntity<String> releaseSeat(@RequestParam Long seatId){
        seatService.releaseSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body("Released seat SuccessFully");
    }

    @GetMapping("/confirm-seat")
    @Operation(summary = "Confirm Seat change seat status from HELD to CONFIRM")
    public ResponseEntity<String> confirmSeat(@RequestParam Long seatId){
        seatService.confirmSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body("Confirm seat SuccessFully");
    }
    @GetMapping("/seat-map")
    public ResponseEntity<List<SeatDto>> getSeatMap(@RequestParam Long flightId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(seatService.getSeatMap(flightId));
    }
    @GetMapping("/count-by-class")
    @Operation(summary = "Count the number of seats by class ")
    public ResponseEntity<Integer> countByClass(@RequestBody RequestCount requestCount){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(seatService.countAvailableByClass(requestCount.getFlightId(),requestCount.getCls()));
    }
    @GetMapping("/search-by-seatNumber")
    @Operation(summary = "Search Seat By SeatNumber")
    public ResponseEntity<SeatDto> getSeatByFlightIdAndSeatNumber(@RequestBody RequestSearchDto requestSearchDto){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatFlightIdAndSeatNumber(requestSearchDto.getFlightId(),requestSearchDto.getSeatNumber()));
    }
}
