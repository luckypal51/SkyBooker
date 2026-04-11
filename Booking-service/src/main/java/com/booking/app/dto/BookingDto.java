package com.booking.app.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
	private String bookingId;
	private Long userId;
	private Long flightId;
	private String pnrCode;
	private String tripType;
	private String status;
	private Double totalFare;
	private Double baseFare;
	private Double taxes;
	private String mealPreference;
	private Integer luggage;
	private String contactEmail;
	private String contactPhone;
	private LocalDateTime bookedAt;
	private String paymentId;
	private String paymentLink;
}
