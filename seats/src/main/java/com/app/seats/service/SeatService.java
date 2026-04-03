package com.app.seats.service;

import com.app.seats.dto.SeatDto;
import com.app.seats.enitity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatService {

    boolean addSeatsForFlight(Long id,List<SeatDto> seats);
    List<SeatDto> getAvailableSeats(Long id);
    List<SeatDto> getAvailableByClass(Long id,String cls);
    SeatDto getSeatById(Long id);
    void holdSeat(Long id);
    void releaseSeat(Long id);
    void confirmSeat(Long id);
    SeatDto updateSeat(Long id, SeatDto seat);
    SeatDto getSeatFlightIdAndSeatNumber(Long id,String seatnumber);
    List<SeatDto> getSeatMap(Long id);
    int countAvailableByClass(Long id, String cls);
    void deleteSeatsForFlight(Long id);

}
