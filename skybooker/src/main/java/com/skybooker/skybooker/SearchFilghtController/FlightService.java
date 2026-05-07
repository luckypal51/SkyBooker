package com.skybooker.skybooker.SearchFilghtController;

import com.skybooker.skybooker.dto.FlightDto;

import com.skybooker.skybooker.dto.TicketSearch;
import com.skybooker.skybooker.service.FlightServiceImp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flight/service")
@Tag(name = "Flight search  APIs")
public class FlightService {
    @Autowired
    FlightServiceImp flightServiceImp;

    @PostMapping("/search-flight-number")
    @Operation(summary = "Search Flights By Flight Number")
    public ResponseEntity<Optional<FlightDto>> searchFlightByNumber(@RequestParam String flightNumber){
        return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.getFlightByNumber(flightNumber));
    }

    @PostMapping("/search-flights")
    @Operation(summary = "Search Flights By Origin, destination and date")
    public ResponseEntity<List<FlightDto>> searchFlight(@RequestBody TicketSearch search){
        return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.searchFlights(search.getOrigin(),search.getDestination(),search.getDate()));
    }

    @PostMapping("/search-flights-airline")
    @Operation(summary = "Search Flights By Airline Id")
    public ResponseEntity<List<FlightDto>> searchFlightByAirlineId(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.getFlightByAirline(id));
    }
    
    @GetMapping("/search-flight-by-id")
    @Operation(summary = "Search Flights By Flight Id")
    public ResponseEntity<Optional<FlightDto>> searchFlightById(@RequestParam Long id){
       return ResponseEntity.status(HttpStatus.ACCEPTED).body(flightServiceImp.getFlightById(id));
    }
}
