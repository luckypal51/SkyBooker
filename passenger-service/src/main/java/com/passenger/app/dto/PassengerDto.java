package com.passenger.app.dto;


import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {

    private Long passengerId;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotBlank(message = "Title is required")
    private String title; 

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    private String lastName;

    @Past(message = "Date of birth must be in past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender; 

    @Pattern(regexp = "^[A-Z0-9]{6,9}$", message = "Invalid passport number")
    private String passportNumber;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @Future(message = "Passport expiry must be in future")
    private LocalDate passportExpiry;

    @NotNull(message = "Seat ID is required")
    private Long seatId;
    
    private String seatNumber;

    private String ticketNumber;

    @NotBlank(message = "Passenger type is required")
    private String passengerType; 
}
