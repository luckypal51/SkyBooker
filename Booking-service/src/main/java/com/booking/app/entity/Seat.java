package com.booking.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
	    private Long seatId;
	    private Long flightId;
	    private String seatNumber;
	    private String seatClass;
	    private int row;
	    private String column;
	    private boolean isWindow;
	    private boolean isAisle;
	    private boolean hasExtraLegroom;
	    private String status;
	    private double priceMultiplier;
}
