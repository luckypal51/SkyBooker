package com.skybooker.skybooker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skybooker.skybooker.entity.Flight;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> {

    Flight findByFlightNumber(String flightName);

    List<Flight> findByAirlineId(Long id);

    List<Flight> findByStatus(String status);

    Flight findByFlightId(Long id);

    @Query("SELECT f FROM Flight f WHERE f.originAirportCode = :origin AND f.destinationAirportCode = :destination AND f.departureDate = :date")
    List<Flight> findFlights(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("date") LocalDate date
    );

    Integer countByAirlineId(Long id);
}
