package com.skybooker.skybooker;


import com.skybooker.skybooker.entity.Flight;
import com.skybooker.skybooker.repository.FlightRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    private Flight createFlight() {
        Flight flight = new Flight();

        flight.setFlightId(1L);
        flight.setFlightNumber("AI101");
        flight.setAirlineId(100L);
        flight.setOriginAirportCode("DEL");
        flight.setDestinationAirportCode("BOM");
        flight.setDepartureDate(LocalDate.now());
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setArrivalDate(LocalDate.now().plusDays(1));
        flight.setArrivalTime(LocalTime.of(12, 30));
        flight.setDurationMinutes(120);
        flight.setStatus("ON_TIME");
        flight.setAircraftType("BOEING");
        flight.setTotalSeats(200);
        flight.setAvailableSeats(150);
        flight.setBasePrice(5000.0);

        return flight;
    }

    @Test
    @DisplayName("Test findByFlightNumber")
    void testFindByFlightNumber() {
        Flight flight = createFlight();
        flightRepository.save(flight);

        Flight result = flightRepository.findByFlightNumber("AI101");

        assertNotNull(result);
        assertEquals("AI101", result.getFlightNumber());
    }

    @Test
    @DisplayName("Test findByAirlineId")
    void testFindByAirlineId() {
        Flight flight = createFlight();
        flightRepository.save(flight);

        List<Flight> result = flightRepository.findByAirlineId(100L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getAirlineId());
    }

    @Test
    @DisplayName("Test findByStatus")
    void testFindByStatus() {
        Flight flight = createFlight();
        flightRepository.save(flight);

        List<Flight> result = flightRepository.findByStatus("ON_TIME");

        assertFalse(result.isEmpty());
        assertEquals("ON_TIME", result.get(0).getStatus());
    }

    @Test
    @DisplayName("Test findByFlightId")
    void testFindByFlightId() {
        Flight flight = createFlight();
        flightRepository.save(flight);

        Flight result = flightRepository.findByFlightId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFlightId());
    }

    @Test
    @DisplayName("Test custom query findFlights")
    void testFindFlights() {
        Flight flight = createFlight();
        flightRepository.save(flight);

        List<Flight> result = flightRepository.findFlights(
                "DEL",
                "BOM",
                LocalDate.now()
        );

        assertFalse(result.isEmpty());
        assertEquals("DEL", result.get(0).getOriginAirportCode());
        assertEquals("BOM", result.get(0).getDestinationAirportCode());
    }

    @Test
    @DisplayName("Test countByAirlineId")
    void testCountByAirlineId() {
        Flight flight = createFlight();
        flightRepository.save(flight);

        Integer count = flightRepository.countByAirlineId(100L);

        assertEquals(1, count);
    }
}