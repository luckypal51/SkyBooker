package com.passenger.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.passenger.app.dto.PassengerDto;
import com.passenger.app.dto.SeatDto;
import com.passenger.app.entity.PassengerInfo;
import com.passenger.app.feigclient.SeatServiceClient;
import com.passenger.app.repository.PassengerRepository;

@Service
public class PassengerServiceImp implements PassengerService{
	
	@Autowired
	SeatServiceClient seatServiceClient;
	
	@Autowired
	PassengerRepository passengerRepo;

	@Override
	public PassengerDto addPassenger(PassengerDto passengerDto) {
		if(validatePassengerData(passengerDto)) {
			passengerRepo.save(convertToPassenger(passengerDto));
           return passengerDto;
		}
		throw new RuntimeException("Passenger Is Not Valid");
	}

	@Override
	public Optional<PassengerDto> getPassengerById(Long passengerId) {
		Optional<PassengerInfo> passengerInfo = passengerRepo.findById(passengerId);
		if(passengerInfo.isPresent()) {
			return Optional.of(convertToDto(passengerInfo.get()));
		}
		throw new RuntimeException("Passenger Not Found With Id "+passengerId);
	}

	@Override
	public List<PassengerDto> getPassengersByBooking(String bookingId) {
		List<PassengerInfo> passengerInfo =  passengerRepo.findByBookingId(bookingId);
		List<PassengerDto> result = new ArrayList<>();
		for(PassengerInfo p: passengerInfo) {
			result.add(convertToDto(p));
		}
		return result;
	}

	@Override
	public Optional<PassengerDto> getByPassportNumber(String passportNumber) {
		Optional<PassengerInfo> passengerInfo = passengerRepo.findByPassportNumber(passportNumber);
		if(passengerInfo.isPresent()) {
			return Optional.of(convertToDto(passengerInfo.get()));
		}
		throw new RuntimeException("Passenger not Found with passportNumber : "+passportNumber);
	}

	@Override
	public PassengerDto updatePassenger(Long passengerId, PassengerDto passengerDto) {
       Optional<PassengerInfo> passenger = passengerRepo.findById(passengerId);
       passengerDto.setPassengerId(passengerId);
       passengerRepo.save(convertToPassenger(passengerDto));
		return passengerDto;
	}

	@Override
	public void assignSeat(Long passengerId, Long seatId, String seatNumber) {
		   Optional<PassengerInfo> passenger = passengerRepo.findById(passengerId);
		   SeatDto seats = seatServiceClient.getSeatById(seatId);
		   if(!seats.getStatus().equalsIgnoreCase("CONFIRMED")&&!seats.getStatus().equalsIgnoreCase("HELD")) {
			   seats.setStatus("CONFIRMED");
			   passenger.get().setSeatId(seats.getSeatId());
			   passenger.get().setSeatNumber(seats.getSeatNumber());
			  
			   seatServiceClient.confirmSeat(seatId);
			   passengerRepo.save(passenger.get());
			   return;
		   }
		   throw new RuntimeException("Seat Is Not Available");
	}

	@Override
	public String generateTicketNumber() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void deletePassenger(Long passengerId) {
		passengerRepo.deleteById(passengerId);
	}

	@Override
	public boolean validatePassengerData(PassengerDto passenger) {
        if(passenger.getDateOfBirth().isAfter(LocalDate.now())) {
        	return false;
        }
        if(passenger.getPassportExpiry().isBefore(LocalDate.now())) {
        	return false;
        }
        return true;
	}

	@Override
	public Integer getPassengerCount(String bookingId) {
		return passengerRepo.countByBookingId(bookingId);
	}
    private PassengerInfo convertToPassenger(PassengerDto passengerDto) {
    	return new PassengerInfo(passengerDto.getPassengerId(),
    			passengerDto.getBookingId(),passengerDto.getTitle(),
    			passengerDto.getFirstName(), passengerDto.getLastName(),
    			passengerDto.getDateOfBirth(),passengerDto.getGender(),
    			passengerDto.getPassportNumber(), passengerDto.getNationality(),
    			passengerDto.getPassportExpiry(), passengerDto.getSeatId(),
    			passengerDto.getSeatNumber(),passengerDto.getTicketNumber(),
    			passengerDto.getPassengerType());
    }
    
    private PassengerDto convertToDto(PassengerInfo passengerInfo) {
    	return new PassengerDto(passengerInfo.getPassengerId(),passengerInfo.getBookingId(),
    			passengerInfo.getTitle(), passengerInfo.getFirstName(),
    			passengerInfo.getLastName(), passengerInfo.getDateOfBirth(),
    			passengerInfo.getGender(), passengerInfo.getPassportNumber(),
    			passengerInfo.getNationality(),passengerInfo.getPassportExpiry(),
    			passengerInfo.getSeatId(),passengerInfo.getSeatNumber(),
    			passengerInfo.getTicketNumber(),passengerInfo.getPassengerType());
    }
}
