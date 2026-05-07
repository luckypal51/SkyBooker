package com.booking.app.interservice;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import com.booking.app.dto.ProductDto;
import com.booking.app.dto.StripeResponse;
import com.booking.app.util.PaymentClientFallback;

@FeignClient(name = "PAYMENT-SERVICE",fallback = PaymentClientFallback.class)
public interface PaymentClient {

	@PostMapping("/payment/checkout")
	public StripeResponse startCheckout(@RequestBody ProductDto product);
}
