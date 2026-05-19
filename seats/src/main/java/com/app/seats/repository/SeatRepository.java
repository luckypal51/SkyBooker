package com.app.seats.repository;

import com.app.seats.enitity.Seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByFlightId(Long id);

    @Query("select s from Seat s where s.flightId=:id AND s.seatClass=:cls")
    List<Seat> findByFlightIdAndSeatClass(@Param("id") Long id,@Param("cls") String cls);

    Optional<Seat> findBySeatId(Long id);

    @Query("select s from Seat s where s.flightId=:id")
    List<Seat> findAvailableByFlightId(@Param("id") Long id);

    @Query("select s from Seat s where s.flightId=:id AND s.seatNumber=:seatNumber")
    Optional<Seat> findByFlightIdAndSeatNumber(@Param("id") Long id,@Param("seatNumber") String seatNumber);

    @Query("select COUNT(s) from Seat s where s.flightId=:id AND s.seatClass=:cls")
    int countAvailableByClass(@Param("id") Long id,@Param("cls") String cls);

    @Modifying
    @Transactional
    @Query("delete from Seat s where s.flightId=:id")
    void deleteByFlightId(@Param("id") Long id);
}
