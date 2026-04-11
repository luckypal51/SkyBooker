package com.booking.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {
      private Long id;
      private String bookingId;
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
