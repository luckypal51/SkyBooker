package com.booking.app.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.app.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>{

	List<Booking> findByUserId(Long id);
	
	Optional<Booking> findByPnrCode(String id);
	
	List<Booking> findByFlightId(Long id);
	
	List<Booking> findByStatus(String status);
	
	Optional<Booking> findByBookingId(Long bookingId);
	
	@Query("select COUNT(b) from Booking b where b.flightId=flightId AND b.status=status")
	Integer countByFlightIdAndStatus(@Param("flightId")Long flightId,@Param("status")String status);
	
	@Query("select COUNT(b) from Booking b where b.userId=userId AND b.status=status")
	List<Booking> findByUserIdAndStatus(@Param("userId")Long userId,@Param("status")String status);
}