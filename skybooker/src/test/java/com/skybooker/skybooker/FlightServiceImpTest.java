package com.skybooker.skybooker;

import com.skybooker.skybooker.dto.FlightDto;
import com.skybooker.skybooker.entity.Flight;
import com.skybooker.skybooker.repository.FlightRepository;
import com.skybooker.skybooker.service.FlightServiceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImpTest {

    @Mock
    private FlightRepository repo;

    @InjectMocks
    private FlightServiceImp flightServiceImp;

    private Flight createFlight() {
        return new Flight(
                1L,
                "AI101",
                100L,
                "DEL",
                "BOM",
                LocalDate.now(),
                LocalTime.of(10, 30),
                LocalDate.now().plusDays(1),
                LocalTime.of(12, 30),
                120,
                "ON_TIME",
                "BOEING",
                200,
                150,
                5000.0
        );
    }

    private FlightDto createFlightDto() {
        return new FlightDto(
                1L,
                "AI101",
                100L,
                "DEL",
                "BOM",
                LocalDate.now(),
                LocalTime.of(10, 30),
                LocalDate.now().plusDays(1),
                LocalTime.of(12, 30),
                120,
                "ON_TIME",
                "BOEING",
                200,
                150,
                5000.0
        );
    }

    @Test
    void testAddFlight() {
        FlightDto dto = createFlightDto();

        when(repo.save(any(Flight.class)))
                .thenReturn(createFlight());

        assertDoesNotThrow(() -> flightServiceImp.addFlight(dto));

        verify(repo, times(1))
                .save(any(Flight.class));
    }

    @Test
    void testGetFlightById() {
        when(repo.findByFlightId(1L))
                .thenReturn(createFlight());

        Optional<FlightDto> result =
                flightServiceImp.getFlightById(1L);

        assertTrue(result.isPresent());
        assertEquals("AI101",
                result.get().getFlightNumber());

        verify(repo, times(1))
                .findByFlightId(1L);
    }

    @Test
    void testGetFlightByNumber() {
        when(repo.findByFlightNumber("AI101"))
                .thenReturn(createFlight());

        Optional<FlightDto> result =
                flightServiceImp.getFlightByNumber("AI101");

        assertTrue(result.isPresent());
        assertEquals("AI101",
                result.get().getFlightNumber());

        verify(repo, times(1))
                .findByFlightNumber("AI101");
    }

    @Test
    void testSearchFlights() {
        when(repo.findFlights(
                "DEL",
                "BOM",
                LocalDate.now()
        )).thenReturn(List.of(createFlight()));

        List<FlightDto> result =
                flightServiceImp.searchFlights(
                        "DEL",
                        "BOM",
                        LocalDate.now()
                );

        assertEquals(1, result.size());

        verify(repo, times(1))
                .findFlights("DEL", "BOM", LocalDate.now());
    }

    @Test
    void testUpdateFlight() {
        FlightDto dto = createFlightDto();

        when(repo.findByFlightId(1L))
                .thenReturn(createFlight());

        when(repo.save(any(Flight.class)))
                .thenReturn(createFlight());

        FlightDto result =
                flightServiceImp.updateFlight(1L, dto);

        assertNotNull(result);
        assertEquals("AI101",
                result.getFlightNumber());

        verify(repo, times(1))
                .findByFlightId(1L);

        verify(repo, times(1))
                .save(any(Flight.class));
    }

    @Test
    void testUpdateStatus() {
        Flight flight = createFlight();

        when(repo.findByFlightId(1L))
                .thenReturn(flight);

        assertDoesNotThrow(() ->
                flightServiceImp.updateStatus(1L, "DELAYED"));

        assertEquals("DELAYED", flight.getStatus());

        verify(repo, times(1))
                .findByFlightId(1L);
    }

    @Test
    void testDecrementSeats() {
        Flight flight = createFlight();
        flight.setAvailableSeats(100);

        when(repo.findByFlightId(1L))
                .thenReturn(flight);

        when(repo.save(any(Flight.class)))
                .thenReturn(flight);

        assertDoesNotThrow(() ->
                flightServiceImp.decrementSeats(1L, 10));

        assertEquals(90, flight.getAvailableSeats());

        verify(repo, times(1))
                .save(any(Flight.class));
    }

    @Test
    void testIncrementSeats() {
        Flight flight = createFlight();
        flight.setAvailableSeats(100);

        when(repo.findByFlightId(1L))
                .thenReturn(flight);

        when(repo.save(any(Flight.class)))
                .thenReturn(flight);

        assertDoesNotThrow(() ->
                flightServiceImp.incrementSeats(1L, 20));

        assertEquals(120, flight.getAvailableSeats());

        verify(repo, times(1))
                .save(any(Flight.class));
    }

    @Test
    void testDeleteFlight() {
        Flight flight = createFlight();

        when(repo.findByFlightId(1L))
                .thenReturn(flight);

        doNothing().when(repo)
                .delete(flight);

        assertDoesNotThrow(() ->
                flightServiceImp.deleteFlight(1L));

        verify(repo, times(1))
                .delete(flight);
    }

    @Test
    void testGetFlightByAirline() {
        when(repo.findByAirlineId(100L))
                .thenReturn(List.of(createFlight()));

        List<FlightDto> result =
                flightServiceImp.getFlightByAirline(100L);

        assertEquals(1, result.size());

        verify(repo, times(1))
                .findByAirlineId(100L);
    }

    @Test
    void testGetAllFlight() {
        when(repo.findAll())
                .thenReturn(List.of(createFlight()));

        List<FlightDto> result =
                flightServiceImp.getAllFlight();

        assertEquals(1, result.size());

        verify(repo, times(1))
                .findAll();
    }
}