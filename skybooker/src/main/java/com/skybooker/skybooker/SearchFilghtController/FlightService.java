package com.skybooker.skybooker.SearchFilghtController;

import com.skybooker.skybooker.entity.Flight;
import com.skybooker.skybooker.model.FlightDto;
import com.skybooker.skybooker.model.TicketSearch;
import com.skybooker.skybooker.service.FlightServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flight/service")
public class FlightService {
    @Autowired
    FlightServiceImp flightServiceImp;

    @PostMapping("/search-flight-number")
    public ResponseEntity<Optional<FlightDto>> searchFlightByNumber(@RequestParam String flightNumber){
        return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.getFlightByNumber(flightNumber));
    }

    @PostMapping("/search-flights")
    public ResponseEntity<List<FlightDto>> searchFlight(@RequestBody TicketSearch search){
        return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.searchFlights(search.getOrigin(),search.getDestination(),search.getDate()));
    }

    @PostMapping("/sreach-flights-airline")
    public ResponseEntity<List<FlightDto>> searchFlightByAirlineId(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(flightServiceImp.getFlightByAirline(id));
    }
}
