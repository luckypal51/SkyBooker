package com.skybooker.skybooker.service;
import com.skybooker.skybooker.entity.Flight;
import com.skybooker.skybooker.model.FlightDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FlightService {
    void addFlight(FlightDto flightDTO);
    Optional<FlightDto> getFlightById(Long id);
    Optional<FlightDto> getFlightByNumber(String flightNumber);
    List<FlightDto> searchFlights(String origin, String destination, LocalDate date);
    Map<String,FlightDto> searchRoundTrip(String origin, String destination, LocalDate departure, LocalDate returnDate);
    FlightDto updateFlight(Long id,FlightDto flight);
    void updateStatus(Long id, String status);
    void decrementSeats(Long id,int seats);
    void incrementSeats(Long id, int seats);
    void deleteFlight(Long id);
    List<FlightDto> getFlightByAirline(Long id);
}
