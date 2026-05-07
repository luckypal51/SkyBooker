package com.booking.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.booking.app.dto.*;
import com.booking.app.entity.Booking;
import com.booking.app.exception.BookingSericeException;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.interservice.*;
import com.booking.app.repository.BookingRepository;
import com.booking.app.service.serviceimpl.BookingServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class BookingServiceApplicationTests{

    @InjectMocks
    private BookingServiceImp service;

    @Mock private FlightClient flightClient;
    @Mock private SeatClient seatClient;
    @Mock private PassengerClient passengerClient;
    @Mock private BookingRepository repo;
    @Mock private PaymentClient pay;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------
    // 1. createBooking success
    // -------------------------
  

    // -------------------------
    // 2. invalid passenger
    // -------------------------
    @Test
    void createBooking_invalidPassenger() {
        PassengerDto p = new PassengerDto();
        p.setPassportExpiry(LocalDate.now().minusDays(1));

        assertThrows(BookingSericeException.class,
                () -> service.createBooking(new BookingDto(), List.of(p)));
    }

    // -------------------------
    // 3. getBookingById success
    // -------------------------
    @Test
    void getBookingById_success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(repo.findByBookingId(1L)).thenReturn(Optional.of(booking));

        BookingDto result = service.getBookingById(1L);

        assertEquals(1L, result.getBookingId());
    }

    // -------------------------
    // 4. getBookingById not found
    // -------------------------
    @Test
    void getBookingById_notFound() {
        when(repo.findByBookingId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getBookingById(1L));
    }

    // -------------------------
    // 5. getBookingByPnr
    // -------------------------
    @Test
    void getBookingByPnr_success() {
        Booking booking = new Booking();
        booking.setPnrCode("ABC123");

        when(repo.findByPnrCode("ABC123")).thenReturn(Optional.of(booking));

        BookingDto result = service.getBookingByPnr("ABC123");

        assertEquals("ABC123", result.getPnrCode());
    }

    // -------------------------
    // 6. cancelBooking
    // -------------------------
    @Test
    void cancelBooking_success() {
        PassengerDto p = new PassengerDto();
        p.setPassengerId(1L);
        p.setSeatId(10L);

        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(passengerClient.getPassengerByBookingId(1L))
                .thenReturn(List.of(p));

        when(repo.findByBookingId(1L)).thenReturn(Optional.of(booking));

        service.cancelBooking(1L);

        verify(seatClient).releaseSeat(10L);
        verify(repo).save(booking);
    }

    // -------------------------
    // 7. confirmBooking
    // -------------------------
   

    // -------------------------
    // 8. updateStatus
    // -------------------------
    @Test
    void updateStatus_success() {
        Booking booking = new Booking();

        when(repo.findByBookingId(1L)).thenReturn(Optional.of(booking));

        service.updateStatus(1L, "CONFIRM");

        assertEquals("CONFIRM", booking.getStatus());
    }

    // -------------------------
    // 9. calculateFare
    // -------------------------
    
    // -------------------------
    // 10. generatePnr
    // -------------------------
    @Test
    void generatePnr_length() {
        String pnr = service.generatePnr();
        assertEquals(6, pnr.length());
    }

    // -------------------------
    // 11. getBookingByUser
    // -------------------------
    @Test
    void getBookingByUser_success() {
        Booking b = new Booking();
        b.setBookingId(1L);

        when(repo.findByUserId(1L)).thenReturn(List.of(b));

        List<BookingDto> result = service.getBookingByUser(1L);

        assertEquals(1, result.size());
    }

    // -------------------------
    // 12. getBookingByFlight
    // -------------------------
    @Test
    void getBookingByFlight_success() {
        Booking b = new Booking();
        b.setFlightId(101L);

        when(repo.findByFlightId(101L)).thenReturn(List.of(b));

        assertEquals(1, service.getBookingByFlight(101L).size());
    }

    // -------------------------
    // 13. getUpcomingBookings
    // -------------------------
    @Test
    void getUpcomingBookings_success() {
        Booking b = new Booking();
        b.setStatus("CONFIRM");

        when(repo.findByUserId(1L)).thenReturn(List.of(b));

        List<BookingDto> result = service.getUpComingBookings(1L);

        assertEquals(1, result.size());
    }

    // -------------------------
    // 14. getUpcomingBookings filter
    // -------------------------
    @Test
    void getUpcomingBookings_filtered() {
        Booking b = new Booking();
        b.setStatus("CANCELLED");

        when(repo.findByUserId(1L)).thenReturn(List.of(b));

        assertEquals(0, service.getUpComingBookings(1L).size());
    }

    // -------------------------
    // 15. holdSeat called
    // -------------------------
   
}