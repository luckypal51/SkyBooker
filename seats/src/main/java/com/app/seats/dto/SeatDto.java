package com.app.seats.dto;

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
    private int row;
    private String column;
    private boolean isWindow;
    private boolean isAisle;
    private boolean hasExtraLegroom;
    private String status;
    private double priceMultiplier;

    public SeatDto(Long flightId, String seatNumber, String seatClass, int row, String column, boolean isWindow, boolean isAisle, boolean hasExtraLegroom, String status, double priceMultiplier) {
        this.flightId = flightId;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.row = row;
        this.column = column;
        this.isWindow = isWindow;
        this.isAisle = isAisle;
        this.hasExtraLegroom = hasExtraLegroom;
        this.status = status;
        this.priceMultiplier = priceMultiplier;
    }
}
