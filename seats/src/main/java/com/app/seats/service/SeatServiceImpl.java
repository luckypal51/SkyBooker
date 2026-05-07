package com.app.seats.service;

import com.app.seats.dto.SeatDto;
import com.app.seats.enitity.Seat;
import com.app.seats.exception.SeatServiceException;
import com.app.seats.repository.SeatRepository;
import com.app.seats.util.ConstantValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class SeatServiceImpl implements SeatService{

    @Autowired
    SeatRepository repo;


    @Override
    @Transactional
    public boolean addSeatsForFlight(Long id, List<SeatDto> seats) {
     
        for(SeatDto s: seats){
            repo.save(convertToSeat(s));
        }
        return true;
    }

    @Override
    public List<SeatDto> getAvailableSeats(Long id) {
       
            List<SeatDto> seatDtoList = new ArrayList<>();
             for( Seat s :repo.findAvailableByFlightId(id)){
                 seatDtoList.add(convertToDto(s));
             }
             
             return seatDtoList;
    }

    @Override
    public List<SeatDto> getAvailableByClass(Long id, String cls) {
     
           List<SeatDto> seatDtoList = new ArrayList<>();
           for (Seat s : repo.findByFlightIdAndSeatClass(id,cls)){
               seatDtoList.add(convertToDto(s));
           }
           return seatDtoList;
      
    }

    @Override
    public SeatDto getSeatById(Long id) {
       
            return convertToDto(repo.findBySeatId(id).get());
       
    }

    @Override
    public void holdSeat(Long id) {
       
            Seat seat = repo.findById(id).orElseThrow(() -> new SeatServiceException(ConstantValue.SEAT_NOT_FOUND_ID + id));
            if(seat.getStatus().equalsIgnoreCase(ConstantValue.CONFIRM)||seat.getStatus().equalsIgnoreCase(ConstantValue.HELD)) {
            	throw new RuntimeException(ConstantValue.SEAT_NOT_AVAILABLE);
            }
            seat.setStatus(ConstantValue.HELD);
            repo.save(seat);
       
    }

    @Override
    public void releaseSeat(Long id) {
    
         Seat seat = repo.findById(id).orElseThrow(() -> new SeatServiceException(ConstantValue.SEAT_NOT_FOUND_ID+ id));
         seat.setStatus(ConstantValue.AVAILABLE);
         repo.save(seat);
    
    }

    @Override
    public void confirmSeat(Long id) {
    
         Seat seat = repo.findById(id).orElseThrow(() -> new SeatServiceException(ConstantValue.SEAT_NOT_FOUND_ID + id));
         seat.setStatus(ConstantValue.CONFIRM);
         repo.save(seat);
     
    }

    @Override
    public SeatDto updateSeat(Long id, SeatDto seat) {
       
            repo.save(convertToSeat(seat));
            return seat;
        
    }

    @Override
    public SeatDto getSeatFlightIdAndSeatNumber(Long id, String seatnumber) {
        return convertToDto(repo.findByFlightIdAndSeatNumber(id,seatnumber).get());
    }

    @Override
    public List<SeatDto> getSeatMap(Long flightId) {

        if (flightId == null) {
            throw new IllegalArgumentException("Flight ID cannot be null");
        }

        List<Seat> seats = repo.findByFlightId(flightId);

        if (seats.isEmpty()) {
            return Collections.emptyList(); // safe return
        }

        return seats.stream()
                .sorted(Comparator.comparing(Seat::getSeatNumber))
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public int countAvailableByClass(Long id, String cls) {
       
            return repo.countAvailableByClass(id,cls);
        
    }

    @Override
    public void deleteSeatsForFlight(Long id) {
   
         repo.deleteByFlightId(id);
     
    }

    private SeatDto convertToDto(Seat seat){
        return new SeatDto(seat.getSeatId(),
                seat.getFlightId(),
                seat.getSeatNumber(),
                seat.getSeatClass(),
                seat.getSeatRow(),
                seat.getSeatColumn(),
                seat.isWindow(),
                seat.isAisle(),
                seat.isHasExtraLegroom(),
                seat.getStatus(),
                seat.getPriceMultiplier());

    }
    private Seat convertToSeat(SeatDto seat){
        return new Seat(seat.getSeatId(),
                seat.getFlightId(),
                seat.getSeatNumber(),
                seat.getSeatClass(),
                seat.getRow(),
                seat.getColumn(),
                seat.isWindow(),
                seat.isAisle(),
                seat.isExtraLegroom(),
                seat.getStatus(),
                seat.getPriceMultiplier());
    }
}
