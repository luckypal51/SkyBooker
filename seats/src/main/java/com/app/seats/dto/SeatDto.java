package com.app.seats.dto;

import lombok.*;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatDto {

    private Long seatId;

    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotBlank(message = "Seat class is required")
    private String seatClass; 

    @Min(value = 1, message = "Row must be at least 1")
    private int row;

    @Pattern(regexp = "^[A-F]$", message = "Column must be between A-F")
    private String column;

    private boolean window;
    private boolean aisle;
    private boolean extraLegroom;

    @NotBlank(message = "Status is required")
    private String status; 

    @Positive
    private Double priceMultiplier;
}
