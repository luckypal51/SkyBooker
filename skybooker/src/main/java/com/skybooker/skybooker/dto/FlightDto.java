package com.skybooker.skybooker.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightDto {

    private long flightId;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Airline ID is required")
    private Long airlineId;

    @NotBlank(message = "Origin airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid airport code")
    private String originAirportCode;

    @NotBlank(message = "Destination airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid airport code")
    private String destinationAirportCode;

    @NotNull
    @FutureOrPresent(message = "Departure date must be present or future")
    private LocalDate departureDate;

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull
    @Future(message = "Arrival date must be in future")
    private LocalDate arrivalDate;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @Min(value = 1, message = "Duration must be positive")
    private long durationMinutes;

    @NotBlank(message = "Status is required")
    private String status; // Better: Enum

    @NotBlank(message = "Aircraft type is required")
    private String aircraftType;

    @Min(value = 1, message = "Total seats must be greater than 0")
    private int totalSeats;

    @Min(value = 0, message = "Available seats cannot be negative")
    private int availableSeats;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private Double basePrice;
}