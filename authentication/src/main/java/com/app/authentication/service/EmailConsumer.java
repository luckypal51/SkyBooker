package com.app.authentication.service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.app.authentication.config.RabbitMQConfig;
import com.app.authentication.dto.EmailEvent;
import com.app.authentication.util.ConstantValue;
@Component
public class EmailConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.FORGOT_PASSWORD_QUEUE)
    public void receive(EmailEvent event) {

        String email = event.getEmail();
        String otp = event.getOtp();

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(ConstantValue.PASSWOR_REQUEST);

        mail.setText(
               ConstantValue.YOUR_OTP+
                otp +
                ConstantValue.VALID_FOR_15_MIN
        );

        mailSender.send(mail);

        System.out.println(ConstantValue.EMAIL_SENT_TO + email);
    }
}