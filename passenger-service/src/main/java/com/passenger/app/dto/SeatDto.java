package com.passenger.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
	private Long seatId;
    private Long flightId;
    private String seatNumber;
    private String seatClass;
    private int seatRow;
    private String seatColumn;
    private boolean isWindow;
    private boolean isAisle;
    private boolean hasExtraLegroom;
    private String status;
    private double priceMultiplier;
}
