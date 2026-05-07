package com.passenger.app.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PassengerInfo {
	@Id
	@GeneratedValue
     private Long passengerId;
     private Long bookingId;
     private String title;
     private String firstName;
     private String lastName;
     private LocalDate dateOfBirth;
     private String gender;
     private String passportNumber;
     private String nationality;
     private LocalDate passportExpiry;
     private Long seatId;
     private String seatNumber;
     private String ticketNumber;
     private String passengerType;
}
