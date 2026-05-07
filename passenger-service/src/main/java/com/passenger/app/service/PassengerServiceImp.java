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
import com.passenger.app.exception.PassengerServiceException;
import com.passenger.app.exception.ResourceNotFoundException;
import com.passenger.app.feigclient.SeatServiceClient;
import com.passenger.app.repository.PassengerRepository;
import com.passenger.app.util.ConstantValue;

@Service
public class PassengerServiceImp implements PassengerService{
	
	@Autowired
	SeatServiceClient seatServiceClient;
	
	@Autowired
	PassengerRepository passengerRepo;

	@Override
	public PassengerDto addPassenger(PassengerDto passengerDto) {
		if(validatePassengerData(passengerDto)) {
			return convertToDto(passengerRepo.save(convertToPassenger(passengerDto))); 
		}
		throw new PassengerServiceException(ConstantValue.INVALID_PASSENGER);
	}

	@Override
	public Optional<PassengerDto> getPassengerById(Long passengerId) {
		Optional<PassengerInfo> passengerInfo = passengerRepo.findById(passengerId);
		if(passengerInfo.isPresent()) {
			return Optional.of(convertToDto(passengerInfo.get()));
		}
		throw new ResourceNotFoundException(ConstantValue.PASSENGER_NOT_FOUND_ID+passengerId);
	}

	@Override
	public List<PassengerDto> getPassengersByBooking(Long bookingId) {
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
		throw new ResourceNotFoundException(ConstantValue.PASSENGER_NOT_FOUND_PASSPORT+passportNumber);
	}

	@Override
	public PassengerDto updatePassenger(Long passengerId, PassengerDto passengerDto) {
       Optional<PassengerInfo> passenger = passengerRepo.findById(passengerId);
       passenger.get().setBookingId(passengerDto.getBookingId());
       passenger.get().setFirstName(passengerDto.getFirstName());
       passenger.get().setLastName(passengerDto.getLastName());
       passenger.get().setPassportExpiry(passengerDto.getPassportExpiry());
       passenger.get().setSeatId(passengerDto.getSeatId());
       passenger.get().setSeatNumber(passengerDto.getSeatNumber());
       passengerRepo.save(passenger.get());
		return passengerDto;
	}

	@Override
	public void assignSeat(Long passengerId, Long seatId, String seatNumber) {
		   Optional<PassengerInfo> passenger = passengerRepo.findById(passengerId);
		   SeatDto seats = seatServiceClient.getSeatById(seatId);
		   if(!seats.getStatus().equalsIgnoreCase(ConstantValue.CONFIRM)&&!seats.getStatus().equalsIgnoreCase(ConstantValue.HELD)) {
			   seats.setStatus(ConstantValue.CONFIRM);
			   passenger.get().setSeatId(seats.getSeatId());
			   passenger.get().setSeatNumber(seatNumber);
			   seatServiceClient.confirmSeat(seatId);
			   passengerRepo.save(passenger.get());
			   return;
		   }
		   throw new PassengerServiceException(ConstantValue.SEAT_NOT_AVAILABLE);
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
	public Integer getPassengerCount(Long bookingId) {
		return passengerRepo.countByBookingId(bookingId);
	}
    private PassengerInfo convertToPassenger(PassengerDto passengerDto) {
    	return new PassengerInfo(passengerDto.getPassengerId(),
    			passengerDto.getBookingId(),passengerDto.getTitle(),
    			passengerDto.getFirstName(), passengerDto.getLastName(),
    			passengerDto.getDateOfBirth(),passengerDto.getGender(),
    			passengerDto.getPassportNumber(), passengerDto.getNationality(),
    			passengerDto.getPassportExpiry(), passengerDto.getSeatId(),passengerDto.getSeatNumber(),
    			passengerDto.getTicketNumber(),
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

	@Override
	public PassengerDto[] addAllPassenger(PassengerDto[] passengerDto) {
	    List<PassengerDto> list = new ArrayList<>();
		for(PassengerDto dto :passengerDto) {
			list.add(addPassenger(dto));
			
		}
		PassengerDto[] pass = list.toArray(new PassengerDto[0]);
		return pass;
	}
}
