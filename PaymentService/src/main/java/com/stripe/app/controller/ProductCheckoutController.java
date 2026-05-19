package com.stripe.app.controller;

import com.stripe.app.dto.PaymentDto;
import com.stripe.app.dto.ProductDto;
import com.stripe.app.dto.StripeResponse;
import com.stripe.app.service.StripeService;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class ProductCheckoutController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/checkout")
    public StripeResponse checkoutProduct(@RequestBody ProductDto productDto){
        return stripeService.checkoutProduct(productDto);
    }
    @GetMapping("/success/{paymentId}")
    public ResponseEntity<Void> successPayment(@PathVariable String paymentId){
    	stripeService.success(paymentId);
    	URI uri = URI.create("http://13.51.35.80/payment/success/"+paymentId);

        return ResponseEntity.status(HttpStatus.FOUND)   // 302
                .location(uri)
                .build();
    }

    @GetMapping("/cancel/{paymentId}")
    public ResponseEntity<PaymentDto> failurePayment(@PathVariable String paymentId){
    	URI uri = URI.create("http://13.51.35.80/payment/failure/"+paymentId);
    	stripeService.failure(paymentId);
        return ResponseEntity.status(HttpStatus.FOUND)   // 302
                .location(uri)
                .build();
        
        
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<String> statusPayment(@PathVariable String paymentId){
        return ResponseEntity.status(HttpStatus.FOUND).body(stripeService.status(paymentId));
    }
}
