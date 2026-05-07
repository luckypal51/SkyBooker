package com.stripe.app.exception;

public class PaymentServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
   
	public PaymentServiceException(String str) {
		super(str);
	}
}
