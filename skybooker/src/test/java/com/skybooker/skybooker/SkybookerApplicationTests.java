package com.skybooker.skybooker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.skybooker.skybooker.dto.FlightDto;
import com.skybooker.skybooker.entity.Flight;
import com.skybooker.skybooker.repository.FlightRepository;
import com.skybooker.skybooker.service.FlightServiceImp;


@SpringBootTest
class SkybookerApplicationTests {

	@Test
	void contextLoads() {
	}
	

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImp flightService;

    public SkybookerApplicationTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFlightByNumber_success() {

        Flight flight = new Flight();
        flight.setFlightNumber("AI101");

        when(flightRepository.findByFlightNumber("AI101"))
                .thenReturn(flight);

        Optional<FlightDto> result = flightService.getFlightByNumber("AI101");
        
        assertTrue(result.isPresent());
        assertEquals("AI101", result.get().getFlightNumber());

        
        verify(flightRepository, times(1)).findByFlightNumber("AI101");
    }
    
    @Test
    void testGetFlightById() {
    	Flight flight = new Flight();
        flight.setFlightId(101L);
        
        when(flightRepository.findByFlightId(101L)).thenReturn(flight);
        
        Optional<FlightDto> result = flightService.getFlightById(101L);
        
        assertTrue(result.isPresent());
        assertEquals(101L, result.get().getFlightId());
    }
    
    @Test
    void testFlightByOriginDestinationDate() {
    	Flight flight = new Flight();
        flight.setFlightId(101L);
        flight.setOriginAirportCode("BHP");
        flight.setDestinationAirportCode("INR");
        flight.setDepartureDate(LocalDate.of(2026, 04, 05));
        List<Flight> list = new ArrayList<>();
        list.add(flight);
        when(flightRepository.findFlights("BHP","INR", LocalDate.of(2026,04,05))).thenReturn(list);
        
        List<Flight> result = flightRepository.findFlights("BHP","INR",LocalDate.of(2026, 04, 05));
        
        assertEquals(1,result.size());
        assertTrue(!result.isEmpty());
    }
    

}
