package com.booking.app.dto;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
      private BookingDto booking;
      private List<PassengerDto> passenger;
}
