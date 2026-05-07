package com.booking.app.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long bookingId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;

    private String pnrCode;

    @NotNull(message = "Trip type is required")
    private String tripType; 

    @NotNull(message = "Status is required")
    private String status; 

    @Positive(message = "Total fare must be positive")
    private Double totalFare;

    @Positive
    private Double baseFare;

    @PositiveOrZero
    private Double taxes;

    private String mealPreference;

    @Min(value = 0, message = "Luggage cannot be negative")
    private Integer luggage;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String contactPhone;

    private LocalDateTime bookedAt; 

    private String paymentId;
    private String paymentLink;
}