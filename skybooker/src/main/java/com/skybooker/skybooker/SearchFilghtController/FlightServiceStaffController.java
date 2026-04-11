package com.skybooker.skybooker.SearchFilghtController;

import com.skybooker.skybooker.model.FlightDto;
import com.skybooker.skybooker.model.RequestFlightDto;
import com.skybooker.skybooker.service.FlightServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/staff/flight")
public class FlightServiceStaffController {
    @Autowired
    FlightServiceImp flightServiceImp;

    @PostMapping("/add")
    public void save(@RequestBody FlightDto flightDto){
       flightServiceImp.addFlight(flightDto);
    }

    @PutMapping("/update-flight")
    public void updateFlight(@RequestParam Long id, @RequestBody FlightDto flightDto){
        flightServiceImp.updateFlight(id,flightDto);
    }

    @PatchMapping("/update-status")
    public void updateStatus(@RequestParam Long id, @RequestParam String status) {
        flightServiceImp.updateStatus(id, status);
    }

    @PostMapping("/decrement-seats")
    public void decrementSeats(@RequestBody RequestFlightDto flightDto){
        flightServiceImp.decrementSeats(flightDto.getId(),flightDto.getSeats());
    }

    @PostMapping("/increment-seats")
    public void incrementSeats(@RequestBody RequestFlightDto flightDto){
        flightServiceImp.incrementSeats(flightDto.getId(),flightDto.getSeats());
    }

    @DeleteMapping("delete-flight")
    public void deleteFlight(@RequestParam Long id){
        flightServiceImp.deleteFlight(id);
    }
}
