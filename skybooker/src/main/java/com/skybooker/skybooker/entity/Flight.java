package com.skybooker.skybooker.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue
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

    public Flight(String flightNumber, long airlineId, String originAirportCode, String destinationAirportCode, LocalDate departureDate, LocalTime departureTime, LocalDate arrivalDate, LocalTime arrivalTime, long durationMinutes, String status, String aircraftType, int totalSeats, int availableSeats, double basePrice) {
        this.flightNumber = flightNumber;
        this.airlineId = airlineId;
        this.originAirportCode = originAirportCode;
        this.destinationAirportCode = destinationAirportCode;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.aircraftType = aircraftType;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.basePrice = basePrice;
    }
}
