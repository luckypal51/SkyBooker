package com.booking.app.service;

import com.booking.app.dto.*;
import com.booking.app.entity.Booking;
import com.booking.app.entity.Seat;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.interservice.*;
import com.booking.app.repository.BookingRepository;
import com.booking.app.service.serviceimpl.BookingServiceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImpTest {

    @InjectMocks
    private BookingServiceImp service;

    @Mock private BookingRepository repo;
    @Mock private FlightClient flightClient;
    @Mock private SeatClient seatClient;
    @Mock private PassengerClient passengerClient;
    @Mock private PaymentClient paymentClient;
    @Mock private NotificationClient notificationClient;

 
    // ---------------- GET BY ID ----------------
    @Test
    void getBookingById_success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setUserId(10L);

        when(repo.findByBookingId(1L))
                .thenReturn(Optional.of(booking));

        BookingDto result = service.getBookingById(1L);

        assertEquals(1L, result.getBookingId());
    }

    @Test
    void getBookingById_not_found() {
        when(repo.findByBookingId(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getBookingById(1L));
    }

    // ---------------- GET BY PNR ----------------
    @Test
    void getBookingByPnr_success() {
        Booking booking = new Booking();
        booking.setPnrCode("ABC123");

        when(repo.findByPnrCode("ABC123"))
                .thenReturn(Optional.of(booking));

        BookingDto result = service.getBookingByPnr("ABC123");

        assertEquals("ABC123", result.getPnrCode());
    }

    // ---------------- CANCEL BOOKING ----------------
    @Test
    void cancelBooking_success() {
        PassengerDto passenger = new PassengerDto();
        passenger.setPassengerId(1L);
        passenger.setSeatId(10L);

        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(passengerClient.getPassengerByBookingId(1L))
                .thenReturn(List.of(passenger));

        when(repo.findByBookingId(1L))
                .thenReturn(Optional.of(booking));

        service.cancelBooking(1L);

        verify(seatClient).releaseSeat(10L);
        verify(passengerClient).updatePassenger(eq(1L), any());
        verify(repo).save(any());
    }

    // ---------------- CONFIRM BOOKING ----------------
    @Test
    void confirmBooking_success() {
        PassengerDto passenger = new PassengerDto();
        passenger.setSeatId(10L);

        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setUserId(5L);
        booking.setContactEmail("test@gmail.com");
        booking.setContactPhone("9876543210");

        when(passengerClient.getPassengerByBookingId(1L))
                .thenReturn(List.of(passenger));

        when(repo.findByBookingId(1L))
                .thenReturn(Optional.of(booking));

        service.confirmBooking(1L);

        verify(seatClient).confirmSeat(10L);
        verify(notificationClient).confirmBooking(any());
        verify(repo).save(any());
    }

    // ---------------- UPDATE STATUS ----------------
    @Test
    void updateStatus_success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(repo.findByBookingId(1L))
                .thenReturn(Optional.of(booking));

        service.updateStatus(1L, "CONFIRMED");

        verify(repo).save(any());
    }

    // ---------------- UPCOMING BOOKINGS ----------------
    @Test
    void upcomingBookings_success() {
        Booking booking = new Booking();
        booking.setStatus("CONFIRM");

        when(repo.findByUserId(1L))
                .thenReturn(List.of(booking));

        List<BookingDto> result = service.getUpComingBookings(1L);

        assertEquals(1, result.size());
    }

    // ---------------- PNR GENERATION ----------------
    @Test
    void generatePnr_length_check() {
        String pnr = service.generatePnr();

        assertNotNull(pnr);
        assertEquals(6, pnr.length());
    }

    // ---------------- TICKET GENERATION ----------------
    @Test
    void create_ticket_success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        PassengerDto passenger = new PassengerDto();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setSeatNumber("12A");

        FlightDto flight = new FlightDto();
        flight.setDepartureDate(LocalDate.now());

        when(repo.findByBookingId(1L))
                .thenReturn(Optional.of(booking));

        when(flightClient.getFlightById(anyLong()))
                .thenReturn(flight);

        when(passengerClient.getPassengerByBookingId(1L))
                .thenReturn(List.of(passenger));

        byte[] result = service.create(1L, 10L);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
