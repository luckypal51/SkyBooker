package com.app.seats.enitity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

   @Id
   @GeneratedValue
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
