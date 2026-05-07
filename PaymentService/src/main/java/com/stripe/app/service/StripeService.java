package com.stripe.app.service;

import com.stripe.Stripe;
import com.stripe.app.client.BookingClient;
import com.stripe.app.dto.PaymentDto;
import com.stripe.app.dto.ProductDto;
import com.stripe.app.dto.StripeResponse;
import com.stripe.app.entity.Payment;
import com.stripe.app.exception.PaymentServiceException;
import com.stripe.app.exception.ResourceNotFoundException;
import com.stripe.app.repository.PaymentRepository;
import com.stripe.app.util.ConstantValue;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StripeService {

    @Autowired
    PaymentRepository repository;
    
    @Autowired
    BookingClient bookingClient;

    //secret key from stripe test account
    @Value("${stripe-api-key}")
    private String apiKeys;

    public StripeResponse checkoutProduct(ProductDto productDto){
        Stripe.apiKey = apiKeys;

        //Payment created
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setCurrency(productDto.getCurrency());
        payment.setAmount(productDto.getAmount());
        payment.setStatus(ConstantValue.PENDING);
        payment.setBookingId(productDto.getBookingId());
        payment.setUserId(productDto.getUserId());
        repository.save(payment);

        //creating the payment in stripe using stripe api's
        SessionCreateParams.LineItem.PriceData.ProductData productData= SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(productDto.getProductName()).build();

       // added amount and currency
        SessionCreateParams.LineItem.PriceData productPrice=  SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(productDto.getCurrency())
                .setUnitAmount((long)( productDto.getAmount()*100))
                .setProductData(productData)
                .build();

       // set the quantity
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity((long) productDto.getQuantity())
                .setPriceData(productPrice).build();

        // creating the after payment login success and failure url
        SessionCreateParams param = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(ConstantValue.SUCCESS_URL+payment.getPaymentId())
                .setCancelUrl(ConstantValue.CANCEL_URL+payment.getPaymentId())
                .addLineItem(lineItem)
                .build();

        Session session = null;
        try{

            session = Session.create(param);
            payment.setTransactionId(session.getId());
            repository.save(payment);

            //return the url of stripe payment gateway for payment
            return StripeResponse.builder().status(ConstantValue.SUCESS).message(ConstantValue.PAYMENT_AMOUNT+productDto.getAmount()).sessionUrl(session.getUrl()).sessionId(session.getId()).build();
        } catch (StripeException e) {
            throw new PaymentServiceException(e.getMessage());
        }
    }

    public PaymentDto success(String paymentId) {
        //after successful payment url will redirect to success url
        Payment payment = repository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException(ConstantValue.PAYMENT_ID_NOT_FOUND));
        bookingClient.confirmBooking(payment.getBookingId());
        payment.setStatus(ConstantValue.SUCESS);
        payment.setPaidAt(LocalDateTime.now());
        repository.save(payment);
        return convertDto(payment);
    }
    public PaymentDto failure(String paymentId){
        //after failure of payment url will redirect to failure url
        Payment payment = repository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException(ConstantValue.PAYMENT_ID_NOT_FOUND));
        bookingClient.confirmBooking(payment.getBookingId());
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus(ConstantValue.CANCEL);
        repository.save(payment);
        return convertDto(payment);
    }

    public String status(String paymentId){
        //to check the status of the payment
        Payment payment = repository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException(ConstantValue.PAYMENT_ID_NOT_FOUND));
        return payment.getStatus();
    }

    public PaymentDto convertDto(Payment payment){
        return PaymentDto.builder().paymentId(payment.getPaymentId())
                .amount(payment.getAmount()).bookingId(payment.getBookingId())
                .paidAt(payment.getPaidAt()).currency(payment.getCurrency())
                .status(payment.getStatus()).transactionId(payment.getTransactionId()).build();
    }
}
