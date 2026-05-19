package com.booking.app.interservice;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.booking.app.config.RabbitMQConfig;
import com.booking.app.dto.BookingConfirmDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendBookingConfirmation(BookingConfirmDto event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
     log.info("Booking Confirm message sent");
      
    }
}