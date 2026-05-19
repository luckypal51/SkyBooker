package com.payment.app.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.payment.app.config.RabbitMQConfig;
import com.payment.app.dto.BookingConfirmDto;
import com.payment.app.service.NotificationService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class BookingNotificationConsumer {
	
	@Autowired
	NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(BookingConfirmDto event) {
       log.info("Message sent to "+event.getEmail());
       notificationService.sendBookingConfirmation(event.getUserId(),event.getBookingId(),event.getEmail(),event.getPhone());
    }
}