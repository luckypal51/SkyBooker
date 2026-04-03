package com.app.seats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSearchDto {
    private Long flightId;
    private String seatNumber;
}
