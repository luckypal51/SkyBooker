package com.booking.app.service.serviceimpl;



import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import com.booking.app.dto.BookingConfirmDto;
import com.booking.app.dto.BookingDto;
import com.booking.app.dto.FareSummary;
import com.booking.app.dto.FlightDto;
import com.booking.app.dto.PassengerDto;
import com.booking.app.dto.ProductDto;
import com.booking.app.dto.StripeResponse;
import com.booking.app.entity.Booking;
import com.booking.app.entity.Seat;
import com.booking.app.exception.BookingSericeException;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.interservice.BookingProducer;
import com.booking.app.interservice.FlightClient;
import com.booking.app.interservice.NotificationClient;
import com.booking.app.interservice.PassengerClient;
import com.booking.app.interservice.PaymentClient;
import com.booking.app.interservice.SeatClient;
import com.booking.app.repository.BookingRepository;
import com.booking.app.service.BookingService;
import com.booking.app.util.ConstantValue;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


@Service
public class BookingServiceImp implements BookingService{
	
	@Autowired
	NotificationClient notify;
	
	@Autowired
	FlightClient flightClient;
	
	@Autowired
	SeatClient seatClient;
	
	@Autowired
	PassengerClient passengerClient;
	
	@Autowired
	BookingRepository repo;
	
	@Autowired
	PaymentClient pay;
	
	@Autowired
	BookingProducer bookingProducer;
	
	private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();
    
    
    @Transactional
	@Override
	public BookingDto createBooking(BookingDto bookingDto, List<PassengerDto> passenagers) {
		   Booking booking = convertBooking(bookingDto);
		   validatePassenger(passenagers);
		   holdSeat(passenagers);
		   booking.setStatus(ConstantValue.PENDING);
		   Booking savedBooking = repo.save(booking);
		   FareSummary fareSummary = calculateFare(booking.getFlightId(),booking.getBookingId(),passenagers.size());
		   savedBooking.setBaseFare(fareSummary.getBaseFare());
		   savedBooking.setTotalFare(fareSummary.getTotalFare());
		   savedBooking.setPnrCode(generatePnr());
		   
		   for(PassengerDto dto : passenagers) {
				dto.setBookingId(savedBooking.getBookingId());
				passengerClient.updatePassenger(dto.getPassengerId(),dto);
			}
		   StripeResponse res = pay.startCheckout(new ProductDto(savedBooking.getUserId(),savedBooking.getBookingId(),ConstantValue.FLIGHT_TICKET,savedBooking.getTotalFare(),1,ConstantValue.INR));
		   
		   if ("FAILED_TEMPORARY".equalsIgnoreCase(res.getStatus())) {
			    savedBooking.setStatus("PAYMENT_PENDING");
			} else if ("SUCCESS".equalsIgnoreCase(res.getStatus())) {
			    savedBooking.setStatus("CONFIRMED");
			} else if ("CANCELLED".equalsIgnoreCase(res.getStatus())) {
			    cancelBooking(savedBooking.getBookingId());
			}
		   savedBooking.setPaymentLink(res.getSessionUrl());
		   savedBooking.setPaymentId(res.getSessionId());
		   repo.save(savedBooking);
		   return convertBookingDto(savedBooking);
	}

	@Override
	public BookingDto getBookingById(Long bookingId) {
	    Booking booking = repo.findByBookingId(bookingId).orElseThrow(()-> new ResourceNotFoundException(ConstantValue.BOOKINGID_NOT_FOUND));
	    return convertBookingDto(booking);
	}

	@Override
	public BookingDto getBookingByPnr(String pnrCode) {
	     return convertBookingDto(repo.findByPnrCode(pnrCode).orElseThrow(()-> new ResourceNotFoundException(ConstantValue.BOOKING_PNR_NOT_FOUND)));	
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
	public void cancelBooking(Long bookingId) {
		List<PassengerDto> passenger = passengerClient.getPassengerByBookingId(bookingId);
		for(PassengerDto dto : passenger) {
			seatClient.releaseSeat(dto.getSeatId());
			dto.setSeatId(null);
			dto.setSeatNumber(null);
			passengerClient.updatePassenger(dto.getPassengerId(),dto);
		}
		Booking booking = repo.findByBookingId(bookingId).orElseThrow(()-> new ResourceNotFoundException(ConstantValue.BOOKINGID_NOT_FOUND));
		booking.setStatus(ConstantValue.CANCELLED);
		repo.save(booking);
		
	}
	public void confirmBooking(Long bookingId) {
		List<PassengerDto> passenger = passengerClient.getPassengerByBookingId(bookingId);
		for(PassengerDto dto : passenger) {
			seatClient.confirmSeat(dto.getSeatId());
		}
		Booking booking = repo.findByBookingId(bookingId).orElseThrow(()-> new ResourceNotFoundException(ConstantValue.BOOKINGID_NOT_FOUND));
		booking.setBookedAt(LocalDateTime.now());
		BookingConfirmDto confirm = new BookingConfirmDto(booking.getUserId(),booking.getBookingId(),booking.getContactEmail(),booking.getContactPhone());
		bookingProducer.sendBookingConfirmation(confirm);
		booking.setStatus(ConstantValue.CONFIRM);
		repo.save(booking);
	}

	@Override
	public void updateStatus(Long bookingId, String status) {
		Optional<Booking> booking = repo.findByBookingId(bookingId);
		booking.get().setStatus(status);
		repo.save(booking.get());
	}

	@Override
	public FareSummary calculateFare(Long flightId, Long bookingId, Integer noPassenger) {
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
		
		return booking.stream().filter((a)->a.getStatus().equalsIgnoreCase(ConstantValue.CONFIRM)).map(s->convertBookingDto(s)).toList();
	}
	
	private void holdSeat(List<PassengerDto> passenger) {
		for(PassengerDto passengerDto: passenger) {
		    seatClient.holdSeat(passengerDto.getSeatId());  
		}
	}
	
	private void validatePassenger(List<PassengerDto> passenger) {
		for(PassengerDto pass: passenger) {
			if(pass==null||pass.getPassportExpiry().isBefore(LocalDate.now())) {
				throw new BookingSericeException(ConstantValue.INVALID_PASSENGER);
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


	@Override
	public void addAddOn(Long bookingId, String type, Double amount) {
		// TODO Auto-generated method stub
		
	}
	public byte[] create(Long bookingId,Long flightId) {
		 System.out.println("bookingId = " + bookingId);
		    System.out.println("flightId = " + flightId);
		FlightDto flightDto = flightClient.getFlightById(flightId);
		 System.out.println("Flight Response = " + flightDto);
		if(flightDto==null) {
			throw new ResourceNotFoundException("Flight is Empty");
		}
		Booking booking = repo.findByBookingId(bookingId).orElseThrow(()-> new ResourceNotFoundException("Booking Not Found"));
		System.out.println("Booking Found = " + booking);
		List<PassengerDto> passenger = passengerClient.getPassengerByBookingId(bookingId);
		 System.out.println("Passengers = " + passenger.size());
		return generateTicket(bookingId,flightDto.getDepartureDate(), passenger);
	}
	
	public byte[] generateTicket(
	        Long bookingId,
	        LocalDate departureDate,
	        List<PassengerDto> passengers) {

	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    PdfWriter writer = new PdfWriter(out);
	    PdfDocument pdf = new PdfDocument(writer);
	    Document document = new Document(pdf);

	    // Removed emoji
	    document.add(new Paragraph("SkyBooker Ticket")
	            .setBold()
	            .setFontSize(18));

	    document.add(new Paragraph("Booking ID: " + bookingId));
	    document.add(new Paragraph("Departure Date: " + departureDate));

	    document.add(new Paragraph("\nPassengers:\n"));

	    float[] columnWidths = {250F, 150F};
	    Table table = new Table(columnWidths);

	    table.addCell("Passenger Name");
	    table.addCell("Seat Number");

	    for (PassengerDto p : passengers) {
	        String fullName = 
	            (p.getFirstName() != null ? p.getFirstName() : "")
	            + " "
	            + (p.getLastName() != null ? p.getLastName() : "");

	        String seat =
	            p.getSeatNumber() != null
	            ? p.getSeatNumber()
	            : "Not Assigned";

	        table.addCell(fullName);
	        table.addCell(seat);
	    }

	    document.add(table);
	    document.close();

	    return out.toByteArray();
	}
}
