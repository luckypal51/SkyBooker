package com.skybooker.skybooker;


import com.skybooker.skybooker.SearchFilghtController.FlightServiceStaffController;
import com.skybooker.skybooker.dto.FlightDto;
import com.skybooker.skybooker.dto.RequestFlightDto;
import com.skybooker.skybooker.service.FlightServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceStaffControllerTest {

    @Mock
    private FlightServiceImp flightServiceImp;

    @InjectMocks
    private FlightServiceStaffController controller;

    @Test
    void testSave() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("AI101");

        doNothing().when(flightServiceImp).addFlight(flightDto);

        controller.save(flightDto);

        verify(flightServiceImp, times(1))
                .addFlight(flightDto);
    }

    @Test
    void testUpdateFlight() {
        Long id = 1L;

        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("AI202");

        when(flightServiceImp.updateFlight(
                eq(id),
                any(FlightDto.class)
        )).thenReturn(flightDto);

        controller.updateFlight(id, flightDto);

        verify(flightServiceImp, times(1))
                .updateFlight(eq(id), any(FlightDto.class));
    }

    @Test
    void testUpdateStatus() {
        Long id = 2L;
        String status = "DELAYED";

        doNothing().when(flightServiceImp)
                .updateStatus(id, status);

        controller.updateStatus(id, status);

        verify(flightServiceImp, times(1))
                .updateStatus(id, status);
    }

    @Test
    void testDecrementSeats() {
        RequestFlightDto request = new RequestFlightDto();
        request.setId(5L);
        request.setSeats(2);

        doNothing().when(flightServiceImp)
                .decrementSeats(5L, 2);

        controller.decrementSeats(request);

        verify(flightServiceImp, times(1))
                .decrementSeats(5L, 2);
    }

    @Test
    void testIncrementSeats() {
        RequestFlightDto request = new RequestFlightDto();
        request.setId(5L);
        request.setSeats(3);

        doNothing().when(flightServiceImp)
                .incrementSeats(5L, 3);

        controller.incrementSeats(request);

        verify(flightServiceImp, times(1))
                .incrementSeats(5L, 3);
    }

    @Test
    void testDeleteFlight() {
        Long id = 10L;

        doNothing().when(flightServiceImp)
                .deleteFlight(id);

        controller.deleteFlight(id);

        verify(flightServiceImp, times(1))
                .deleteFlight(id);
    }

    @Test
    void testGetAllFlight() {
        FlightDto flight1 = new FlightDto();
        flight1.setFlightNumber("AI111");

        FlightDto flight2 = new FlightDto();
        flight2.setFlightNumber("AI222");

        List<FlightDto> flights = List.of(flight1, flight2);

        when(flightServiceImp.getAllFlight())
                .thenReturn(flights);

        ResponseEntity<List<FlightDto>> response =
                controller.getAllFlight();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(flightServiceImp, times(1))
                .getAllFlight();
    }
}