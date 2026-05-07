package com.booking.app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FlightDto {
	 private long flightId;
	    private String flightNumber;
	    private long airlineId;
	    private String originAirportCode;
	    private String destinationAirportCode;
	    private LocalDate departureDate;
	    private LocalTime departureTime;
	    private LocalDate arrivalDate;
	    private LocalTime arrivalTime;
	    private long durationMinutes;
	    private String status;
	    private String aircraftType;
	    private int totalSeats;
	    private int availableSeats;
	    private double basePrice;
}
