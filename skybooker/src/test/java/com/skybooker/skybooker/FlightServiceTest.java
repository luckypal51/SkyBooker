package com.skybooker.skybooker;

import com.skybooker.skybooker.SearchFilghtController.FlightService;
import com.skybooker.skybooker.dto.FlightDto;
import com.skybooker.skybooker.dto.TicketSearch;
import com.skybooker.skybooker.service.FlightServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightServiceImp flightServiceImp;

    @InjectMocks
    private FlightService flightService;

    @Test
    void testSearchFlightByNumber() {
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("AI101");

        when(flightServiceImp.getFlightByNumber("AI101"))
                .thenReturn(Optional.of(flightDto));

        ResponseEntity<Optional<FlightDto>> response =
                flightService.searchFlightByNumber("AI101");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals("AI101", response.getBody().get().getFlightNumber());

        verify(flightServiceImp, times(1))
                .getFlightByNumber("AI101");
    }

    @Test
    void testSearchFlight() {
        TicketSearch search = new TicketSearch();
        search.setOrigin("DEL");
        search.setDestination("BOM");
        search.setDate(LocalDate.now());

        FlightDto flight1 = new FlightDto();
        flight1.setFlightNumber("AI101");

        FlightDto flight2 = new FlightDto();
        flight2.setFlightNumber("AI102");

        List<FlightDto> flightList = List.of(flight1, flight2);

        when(flightServiceImp.searchFlights(
                "DEL",
                "BOM",
                search.getDate()
        )).thenReturn(flightList);

        ResponseEntity<List<FlightDto>> response =
                flightService.searchFlight(search);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(flightServiceImp, times(1))
                .searchFlights("DEL", "BOM", search.getDate());
    }

    @Test
    void testSearchFlightByAirlineId() {
        Long airlineId = 1L;

        FlightDto flightDto = new FlightDto();
        flightDto.setFlightNumber("AI500");

        List<FlightDto> list = List.of(flightDto);

        when(flightServiceImp.getFlightByAirline(airlineId))
                .thenReturn(list);

        ResponseEntity<List<FlightDto>> response =
                flightService.searchFlightByAirlineId(airlineId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(flightServiceImp, times(1))
                .getFlightByAirline(airlineId);
    }

    @Test
    void testSearchFlightById() {
        Long id = 10L;

        FlightDto flightDto = new FlightDto();
        flightDto.setFlightId(id);
        flightDto.setFlightNumber("AI777");

        when(flightServiceImp.getFlightById(id))
                .thenReturn(Optional.of(flightDto));

        ResponseEntity<Optional<FlightDto>> response =
                flightService.searchFlightById(id);

        assertEquals(202, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals(id, response.getBody().get().getFlightId());

        verify(flightServiceImp, times(1))
                .getFlightById(id);
    }
}