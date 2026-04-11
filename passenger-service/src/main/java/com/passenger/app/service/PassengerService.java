package com.passenger.app.service;

import java.util.List;
import java.util.Optional;

import com.passenger.app.dto.PassengerDto;
import com.passenger.app.entity.PassengerInfo;

public interface PassengerService {
	
	PassengerDto addPassenger(PassengerDto passengerDto);
	
	Optional<PassengerDto> getPassengerById(Long passengerId);
	
	List<PassengerDto> getPassengersByBooking(String bookingId);
    
	Optional<PassengerDto> getByPassportNumber(String passportNumber);
	
	PassengerDto updatePassenger(Long passengerId,PassengerDto passengerDto);
	
	void assignSeat(Long passengerId,Long seatId,String seatNumber);
	
	String generateTicketNumber();
	
	void deletePassenger(Long passengerId);
	
	boolean validatePassengerData(PassengerDto passengerDto);
	
	Integer getPassengerCount(String bookingId);
}
