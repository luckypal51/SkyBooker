package com.app.seats.exception;

public class SeatServiceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
    
	public SeatServiceException(String str) {
		super(str);
	}
}
