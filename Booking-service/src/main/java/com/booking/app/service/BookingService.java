package com.booking.app.service;

import java.util.*;

import com.booking.app.dto.BookingDto;
import com.booking.app.dto.FareSummary;
import com.booking.app.dto.PassengerDto;
import com.booking.app.entity.Booking;

public interface BookingService {
	
	BookingDto createBooking(BookingDto booking,List<PassengerDto> passenagers);
    
	BookingDto getBookingById(Long bookingId);
	
	BookingDto getBookingByPnr(String pnrCode);
	
	List<BookingDto> getBookingByUser(Long userId);
	
	List<BookingDto> getBookingByFlight(Long flightId);
	
	void cancelBooking(Long bookingId);
	
	void updateStatus(Long bookingId,String status);
	
	FareSummary calculateFare(Long flightId,Long classs,Integer noPassenger);
	
	void addAddOn(Long bookingId,String type,Double amount);
	
	String generatePnr();
	
	List<BookingDto> getUpComingBookings(Long userId);
	
}
