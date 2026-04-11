package com.booking.app.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.app.dto.BookingDto;
import com.booking.app.dto.FareSummary;
import com.booking.app.dto.PassengerDto;
import com.booking.app.entity.Booking;
import com.booking.app.entity.Seat;
import com.booking.app.interservice.FlightClient;
import com.booking.app.interservice.PassengerClient;
import com.booking.app.interservice.SeatClient;
import com.booking.app.repository.BookingRepository;

@Service
public class BookingServiceImp implements BookingService{
	
	@Autowired
	FlightClient flightClient;
	
	@Autowired
	SeatClient seatClient;
	
	@Autowired
	PassengerClient passengerClient;
	
	@Autowired
	BookingRepository repo;
	
	private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();

	@Override
	public BookingDto createBooking(BookingDto bookingDto, List<PassengerDto> passenagers) {
		   Booking booking = convertBooking(bookingDto);
		   validatePassenger(passenagers);
		   holdSeat(passenagers);
		   booking.setStatus("PENDING");
		   Booking savedBooking = repo.save(booking);
		   FareSummary fareSummary = calculateFare(booking.getFlightId(),booking.getBookingId(),passenagers.size());
		   savedBooking.setBaseFare(fareSummary.getBaseFare());
		   savedBooking.setTotalFare(fareSummary.getTotalFare());
		   savedBooking.setPnrCode(generatePnr());
		   
		   repo.save(savedBooking);
		   return convertBookingDto(savedBooking);
	}

	@Override
	public BookingDto getBookingById(String bookingId) {
	    Optional<Booking> booking = repo.findByBookingId(bookingId);
	    return convertBookingDto(booking.get());
	}

	@Override
	public BookingDto getBookingByPnr(String pnrCode) {
	     return convertBookingDto(repo.fingByPnrCode(pnrCode).get());	
	}

	@Override
	public List<BookingDto> getBookingByUser(Long userId) {
		// TODO Auto-generated method stub
		return repo.findByUserId(userId).stream().map(c->convertBookingDto(c)).toList();
	}

	@Override
	public List<BookingDto> getBookingByFlight(Long flightId) {
		// TODO Auto-generated method stub
		return repo.findByFlightId(flightId).stream().map(c->convertBookingDto(c)).toList();
	}

	@Override
	public void cancelBooking(String bookingId) {
		List<PassengerDto> passenger = passengerClient.getPassengerByBookingId(bookingId);
		for(PassengerDto dto : passenger) {
			seatClient.releaseSeat(dto.getSeatId());
			dto.setSeatId(null);
			dto.setSeatNumber(null);
			passengerClient.updatePassenger(dto);
		}
		Booking booking = repo.findByBookingId(bookingId).orElseThrow(()-> new RuntimeException("Booking Is Null"));
		booking.setStatus("CANCELLED");
		repo.save(booking);
		
	}

	@Override
	public void updateStatus(String bookingId, String status) {
		Optional<Booking> booking = repo.findByBookingId(bookingId);
		booking.get().setStatus(status);
		repo.save(booking.get());
	}

	@Override
	public FareSummary calculateFare(Long flightId, String bookingId, Integer noPassenger) {
		FareSummary fareSummary = new FareSummary();
		Optional<Booking> booking = repo.findByBookingId(bookingId);
		Double amount = flightClient.getFlightById(flightId).getBasePrice()+(booking.get().getLuggage()*150);
		amount = amount*noPassenger;
		fareSummary.setBaseFare(amount);
		fareSummary.setTaxes((amount/100)*18);
		fareSummary.setTotalFare(fareSummary.getBaseFare()+fareSummary.getTaxes());
		
		return fareSummary;
	}

	@Override
	public void addAddOn(String bookingId, String type, Double amount) {
	}

	@Override
	public String generatePnr() {
		// Generates a 6-character random string
        return random.ints(6, 0, ALPHA_NUMERIC.length())
                .mapToObj(ALPHA_NUMERIC::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
	}

	@Override
	public List<BookingDto> getUpComingBookings(Long userId) {
		List<Booking> booking = repo.findByUserId(userId);
		
		return booking.stream().filter((a)->a.getStatus().equalsIgnoreCase("CONFIRM")).map(s->convertBookingDto(s)).toList();
	}
	
	private void holdSeat(List<PassengerDto> passenger) {
		for(PassengerDto passengerDto: passenger) {
		    seatClient.holdSeat(passengerDto.getSeatId());  
		}
	}
	
	private void validatePassenger(List<PassengerDto> passenger) {
		for(PassengerDto pass: passenger) {
			if(pass==null||pass.getPassportExpiry().isBefore(LocalDate.now())) {
				throw new RuntimeException("User Not Valid");
			}
		}
	}
	
	private BookingDto convertBookingDto(Booking booking) {
		BookingDto bookingDto = new BookingDto(booking.getBookingId(),booking.getUserId(),booking.getFlightId(),
				booking.getPnrCode(),booking.getTripType(),booking.getStatus(),booking.getTotalFare(),
				booking.getBaseFare(),booking.getTaxes(),booking.getMealPreference(),booking.getLuggage(),
				booking.getContactEmail(),booking.getContactPhone(),booking.getBookedAt(),booking.getPaymentId(),
				booking.getPaymentLink());
		return bookingDto;
	}
	
	public Booking convertBooking(BookingDto bookingDto) {
		Booking booking = new Booking(bookingDto.getBookingId(),bookingDto.getUserId(),bookingDto.getFlightId(),
				bookingDto.getPnrCode(),bookingDto.getTripType(),bookingDto.getStatus(),bookingDto.getTotalFare(),
				bookingDto.getBaseFare(),bookingDto.getTaxes(),bookingDto.getMealPreference(),bookingDto.getLuggage(),
				bookingDto.getContactEmail(),bookingDto.getContactPhone(),bookingDto.getBookedAt(),bookingDto.getPaymentId(),
				bookingDto.getPaymentLink());
		return booking;
	}
}
