package com.app.seats.controller;

import com.app.seats.dto.RequestCount;
import com.app.seats.dto.RequestDto;
import com.app.seats.dto.RequestSearchDto;
import com.app.seats.dto.SeatDto;
import com.app.seats.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {
    @Autowired
    SeatService seatService;

    @GetMapping("/available-seats")
    public ResponseEntity<List<SeatDto>> getAvailable(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getAvailableSeats(id));
    }

    @GetMapping("/available-class")
    public ResponseEntity<List<SeatDto>> getAvailableSeatByClass(@RequestBody RequestDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getAvailableByClass(dto.getFlightid(), dto.getCls()));
    }

    @GetMapping("/search-id")
    public ResponseEntity<SeatDto> getById(@RequestParam Long seatId){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatById(seatId));
    }

    @GetMapping("/hold-seat")
    public ResponseEntity<String> holdSeat(@RequestParam Long seatId){
        seatService.holdSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body("Held Seat SuccessFully");
    }
    @GetMapping("/release-seat")
    public ResponseEntity<String> releaseSeat(@RequestParam Long seatId){
        seatService.releaseSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body("Released seat SuccessFully");
    }

    @GetMapping("/confirm-seat")
    public ResponseEntity<String> confirmSeat(@RequestParam Long seatId){
        seatService.confirmSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body("Confirm seat SuccessFully");
    }
    @GetMapping("/seat-map")
    public ResponseEntity<List<SeatDto>> getSeatMap(@RequestParam Long flightId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(seatService.getSeatMap(flightId));
    }
    @GetMapping("/count-by-class")
    public ResponseEntity<Integer> countByClass(@RequestBody RequestCount requestCount){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(seatService.countAvailableByClass(requestCount.getFlightId(),requestCount.getCls()));
    }
    @GetMapping("/search-by-seatNumber")
    public ResponseEntity<SeatDto> getSeatByFlightIdAndSeatNumber(@RequestBody RequestSearchDto requestSearchDto){
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatFlightIdAndSeatNumber(requestSearchDto.getFlightId(),requestSearchDto.getSeatNumber()));
    }
}
