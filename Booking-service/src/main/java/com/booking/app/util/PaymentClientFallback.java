package com.booking.app.util;

import org.springframework.stereotype.Component;

import com.booking.app.dto.ProductDto;
import com.booking.app.dto.StripeResponse;
import com.booking.app.interservice.PaymentClient;

@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public StripeResponse startCheckout(ProductDto product) {
        return StripeResponse.builder().message("PaymentService Down").status("FAILED_TEMPORARY").build();
    }
}