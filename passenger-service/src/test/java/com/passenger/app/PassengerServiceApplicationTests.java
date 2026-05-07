package com.passenger.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.passenger.app.dto.PassengerDto;
import com.passenger.app.entity.PassengerInfo;
import com.passenger.app.repository.PassengerRepository;
import com.passenger.app.service.PassengerServiceImp;

@SpringBootTest
class PassengerServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	PassengerRepository repo;
	
	@InjectMocks
	PassengerServiceImp passengerServiceImp;
	
	@Test
	void testGetPassengerById() {
		PassengerInfo passenger = new PassengerInfo();
		passenger.setPassengerId(101L);
		passenger.setFirstName("Lucky");
		
		when(repo.findById(101L)).thenReturn(Optional.of(passenger));
		
		PassengerDto result = passengerServiceImp.getPassengerById(101L).get();
		
		assertEquals("Lucky",result.getFirstName());
   }
	
	@Test
	void testGetPassengerByBookingId() {
		List<PassengerInfo> passenger = new ArrayList<>();
		PassengerInfo pass1 = new PassengerInfo();
		pass1.setBookingId(102L);
		pass1.setFirstName("Lucky");
		pass1.setPassengerId(101L);
		PassengerInfo pass2 = new PassengerInfo();
		pass2.setBookingId(102L);
		pass2.setFirstName("Himesh");
		pass2.setPassengerId(102L);
		PassengerInfo pass3 = new PassengerInfo();
		pass3.setBookingId(102L);
		pass3.setFirstName("Nageshwar");
		pass3.setPassengerId(103L);
		
		passenger.add(pass1);
		passenger.add(pass2);
		passenger.add(pass3);
		
		when(repo.findByBookingId(102L)).thenReturn(passenger);
		
		List<PassengerDto> result = passengerServiceImp.getPassengersByBooking(102L);
		
		assertEquals(3,result.size());
		
		assertEquals("Lucky",result.get(0).getFirstName());
		assertEquals("Himesh",result.get(1).getFirstName());
		assertEquals("Nageshwar", result.get(2).getFirstName());
	}
	
	@Test
	void testValidatePassenger() {
		PassengerDto pass = new PassengerDto();
		pass.setDateOfBirth(LocalDate.of(2003, 12, 05));
		pass.setPassportExpiry(LocalDate.of(2028,12, 05));
		
		assertTrue(passengerServiceImp.validatePassengerData(pass));
	}
}
