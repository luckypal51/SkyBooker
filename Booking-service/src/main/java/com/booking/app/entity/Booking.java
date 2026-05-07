package com.booking.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
     
	@Id
	@GeneratedValue
	private Long bookingId;
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
	@Column(columnDefinition = "TEXT")
	private String paymentLink;
	
}
