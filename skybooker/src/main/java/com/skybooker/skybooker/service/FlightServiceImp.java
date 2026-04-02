package com.skybooker.skybooker.service;

import com.skybooker.skybooker.entity.Flight;
import com.skybooker.skybooker.model.FlightDto;
import com.skybooker.skybooker.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class FlightServiceImp implements FlightService{

    @Autowired
    FlightRepository repo;


    @Override
    public void addFlight(FlightDto flightDTO) {
       try{
           repo.save(convertToFlight(flightDTO));
       } catch (Exception e) {
           throw new RuntimeException("Unable to add flight data");
       }
    }

    @Override
    public Optional<FlightDto> getFlightById(Long id) {
        try{
            FlightDto flight = convertToFlightDto(repo.findByFlightId(id));
            return Optional.of(flight);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<FlightDto> getFlightByNumber(String flightNumber) {
        try{
            FlightDto flight = convertToFlightDto(repo.findByFlightNumber(flightNumber));
            return Optional.of(flight);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FlightDto> searchFlights(String origin, String destination, LocalDate date) {
       try{
           List<Flight> flights = repo.findFlights(origin, destination, date);
           List<FlightDto> flightDtos = new ArrayList<>();
           for (Flight f: flights){
               flightDtos.add(convertToFlightDto(f));
           }
           return flightDtos;
       } catch (RuntimeException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public Map<String, FlightDto> searchRoundTrip(String origin, String destination, LocalDate departure, LocalDate returnDate) {
        return Map.of();
    }

    @Override
    public FlightDto updateFlight(Long id, FlightDto flight) {
        try{
            Flight oldFLight = repo.findByFlightId(id);
            repo.delete(oldFLight);
            repo.save(convertToFlight(flight));
            return flight;
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to update data");
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        try{
            Flight oldFlight = repo.findByFlightId(id);
            oldFlight.setStatus(status);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void decrementSeats(Long id, int seats) {
        try{
            Flight flight = repo.findByFlightId(id);
            if(flight.getAvailableSeats()<seats){
                throw  new RuntimeException("Seats Not Available");
            }
            flight.setAvailableSeats((flight.getAvailableSeats()-seats));
            repo.save(flight);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void incrementSeats(Long id, int seats) {
        try{
            Flight flight = repo.findByFlightId(id);
            flight.setAvailableSeats((flight.getAvailableSeats()+seats));
            repo.save(flight);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFlight(Long id) {
     try{
         Flight flight = repo.findByFlightId(id);
         repo.delete(flight);
     } catch (RuntimeException e) {
         throw new RuntimeException(e);
     }
    }

    @Override
    public List<FlightDto> getFlightByAirline(Long id) {
        try{
            List<Flight> flights = repo.findByAirlineId(id);
            List<FlightDto> flightDtos = new ArrayList<>();
            for(Flight f: flights){
                flightDtos.add(convertToFlightDto(f));
            }
            return flightDtos;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private Flight convertToFlight(FlightDto flightDto){
        return new Flight(
                flightDto.getFlightNumber(),
                flightDto.getAirlineId(),
                flightDto.getOriginAirportCode(),
                flightDto.getDestinationAirportCode(),
                flightDto.getDepartureDate(),
                flightDto.getDepartureTime(),
                flightDto.getArrivalDate(),
                flightDto.getArrivalTime(),
                flightDto.getDurationMinutes(),
                flightDto.getStatus(),
                flightDto.getAircraftType(),
                flightDto.getTotalSeats(),
                flightDto.getAvailableSeats(),
                flightDto.getBasePrice());
    }

    private FlightDto convertToFlightDto(Flight flight){
        return new FlightDto(  flight.getFlightNumber(),
                flight.getAirlineId(),
                flight.getOriginAirportCode(),
                flight.getDestinationAirportCode(),
                flight.getDepartureDate(),
                flight.getDepartureTime(),
                flight.getArrivalDate(),
                flight.getArrivalTime(),
                flight.getDurationMinutes(),
                flight.getStatus(),
                flight.getAircraftType(),
                flight.getTotalSeats(),
                flight.getAvailableSeats(),
                flight.getBasePrice());
    }
}
