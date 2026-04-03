package com.app.seats.service;

import com.app.seats.dto.SeatDto;
import com.app.seats.enitity.Seat;
import com.app.seats.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class SeatServiceImpl implements SeatService{

    @Autowired
    SeatRepository repo;


    @Override
    @Transactional
    public boolean addSeatsForFlight(Long id, List<SeatDto> seats) {
        try{
        for(SeatDto s: seats){
            repo.save(convertToSeat(s));
        }
        return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public List<SeatDto> getAvailableSeats(Long id) {
        try{
            List<SeatDto> seatDtoList = new ArrayList<>();
             for( Seat s :repo.findAvailableByFlightId(id)){
                 seatDtoList.add(convertToDto(s));
             }
             return seatDtoList;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SeatDto> getAvailableByClass(Long id, String cls) {
       try{
           List<SeatDto> seatDtoList = new ArrayList<>();
           for (Seat s : repo.findByFlightIdAndSeatClass(id,cls)){
               seatDtoList.add(convertToDto(s));
           }
           return seatDtoList;
       } catch (RuntimeException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public SeatDto getSeatById(Long id) {
        try{
            return convertToDto(repo.findBySeatId(id).get());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void holdSeat(Long id) {
        try{
            Seat seat = repo.findById(id).orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
            seat.setStatus("HELD");
            repo.save(seat);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void releaseSeat(Long id) {
     try{
         Seat seat = repo.findById(id).orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
         seat.setStatus("AVAILABLE");
         repo.save(seat);
     } catch (RuntimeException e) {
         throw new RuntimeException(e);
     }
    }

    @Override
    public void confirmSeat(Long id) {
     try{
         Seat seat = repo.findById(id).orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
         seat.setStatus("BOOKED");
         repo.save(seat);
     } catch (RuntimeException e) {
         throw new RuntimeException(e);
     }
    }

    @Override
    public SeatDto updateSeat(Long id, SeatDto seat) {
        try{
            repo.save(convertToSeat(seat));
            return seat;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeatDto getSeatFlightIdAndSeatNumber(Long id, String seatnumber) {
        return convertToDto(repo.findByFlightIdAndSeatNumber(id,seatnumber).get());
    }

    @Override
    public List<SeatDto> getSeatMap(Long id) {
       try{
           List<Seat> seats = repo.findByFlightId(id);
           List<SeatDto> seatDtoList = new ArrayList<>();
           for (Seat s: seats){
               seatDtoList.add(convertToDto(s));
           }
           return seatDtoList;
       } catch (RuntimeException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public int countAvailableByClass(Long id, String cls) {
        try{
            return repo.countAvailableByClass(id,cls);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSeatsForFlight(Long id) {
     try{
         repo.deleteByFlightId(id);
     } catch (RuntimeException e) {
         throw new RuntimeException(e);
     }
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
                seat.isHasExtraLegroom(),
                seat.getStatus(),
                seat.getPriceMultiplier());
    }
}
