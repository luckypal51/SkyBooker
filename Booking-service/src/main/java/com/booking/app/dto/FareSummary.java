package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FareSummary {
	    private double baseFare;
	    private double taxes;
	    private double discount;
	    private double totalFare;

}
