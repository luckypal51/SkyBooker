package com.booking.app.controller;


import com.booking.app.dto.*;
import com.booking.app.service.serviceimpl.BookingServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceImp bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------- CREATE BOOKING ----------------
    @Test
    void testCreateBooking_success() throws Exception {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setUserId(1L);
        bookingDto.setFlightId(10L);
        bookingDto.setTripType("ONE_WAY");
        bookingDto.setStatus("PENDING");
        bookingDto.setContactEmail("test@gmail.com");
        bookingDto.setContactPhone("9876543210");

        PassengerDto passenger = new PassengerDto();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setPassengerType("ADULT");

        RequestDto requestDto = new RequestDto(bookingDto, List.of(passenger));

        Mockito.when(bookingService.createBooking(any(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    // ---------------- CANCEL BOOKING ----------------
    @Test
    void testCancelBooking_success() throws Exception {

        Mockito.doNothing().when(bookingService).cancelBooking(1L);

        mockMvc.perform(delete("/booking/cancel")
                        .param("bookingId", "1"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookingService).cancelBooking(1L);
    }

    // ---------------- CONFIRM BOOKING ----------------
    @Test
    void testConfirmBooking_success() throws Exception {

        Mockito.doNothing().when(bookingService).confirmBooking(1L);

        mockMvc.perform(get("/booking/confirm")
                        .param("bookingId", "1"))
                .andExpect(status().isOk());

        Mockito.verify(bookingService).confirmBooking(1L);
    }

    // ---------------- VIEW BY USER ID ----------------
    @Test
    void testGetBookingsByUser_success() throws Exception {

        BookingDto booking = new BookingDto();
        booking.setBookingId(1L);

        Mockito.when(bookingService.getBookingByUser(1L))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/booking/view-booking-userId")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1L));
    }

    // ---------------- VIEW BY FLIGHT ID ----------------
    @Test
    void testGetBookingsByFlight_success() throws Exception {

        Mockito.when(bookingService.getBookingByFlight(10L))
                .thenReturn(List.of(new BookingDto()));

        mockMvc.perform(get("/booking/view-booking-flightId")
                        .param("flightId", "10"))
                .andExpect(status().isFound());
    }

    // ---------------- VIEW BY BOOKING ID ----------------
    @Test
    void testGetBookingById_success() throws Exception {

        BookingDto booking = new BookingDto();
        booking.setBookingId(1L);

        Mockito.when(bookingService.getBookingById(1L))
                .thenReturn(booking);

        mockMvc.perform(get("/booking/view-booking-bookingId")
                        .param("bookingId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L));
    }

    // ---------------- VIEW BY PNR ----------------
    @Test
    void testGetBookingByPnr_success() throws Exception {

        BookingDto booking = new BookingDto();
        booking.setPnrCode("PNR123");

        Mockito.when(bookingService.getBookingByPnr("PNR123"))
                .thenReturn(booking);

        mockMvc.perform(get("/booking/view-booking-pnr")
                        .param("pnrCode", "PNR123"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.pnrCode").value("PNR123"));
    }

    // ---------------- UPCOMING BOOKINGS ----------------
    @Test
    void testGetUpcomingBookings_success() throws Exception {

        Mockito.when(bookingService.getUpComingBookings(1L))
                .thenReturn(List.of(new BookingDto()));

        mockMvc.perform(get("/booking/view-booking-upcoming")
                        .param("userId", "1"))
                .andExpect(status().isFound());
    }

    // ---------------- UPDATE STATUS ----------------
    @Test
    void testUpdateBookingStatus_success() throws Exception {

        RequestStatus status = new RequestStatus();
        status.setBookingId(1L);
        status.setStatus("CONFIRMED");

        Mockito.doNothing().when(bookingService)
                .updateStatus(1L, "CONFIRMED");

        mockMvc.perform(put("/booking/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk());

        Mockito.verify(bookingService).updateStatus(1L, "CONFIRMED");
    }

    // ---------------- TICKET GENERATION ----------------
    @Test
    void testGetTicket_success() throws Exception {

        byte[] pdf = "dummy-pdf".getBytes();

        Mockito.when(bookingService.create(1L, 10L))
                .thenReturn(pdf);

        mockMvc.perform(get("/booking/ticket")
                        .param("bookingId", "1")
                        .param("flightId", "10"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=ticket.pdf"));
    }
}