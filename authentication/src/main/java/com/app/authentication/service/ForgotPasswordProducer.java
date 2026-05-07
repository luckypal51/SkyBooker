package com.app.authentication.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.authentication.config.RabbitMQConfig;
import com.app.authentication.dto.EmailEvent;

@Service
public class ForgotPasswordProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailEvent(String email, String otp) {

        EmailEvent event = new EmailEvent();
        event.setEmail(email);
        event.setOtp(otp);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FORGOT_PASSWORD_QUEUE,
                event
        );
    }
}