package com.app.authentication.service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.app.authentication.config.RabbitMQConfig;
import com.app.authentication.dto.EmailEvent;
import com.app.authentication.util.ConstantValue;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class EmailConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.FORGOT_PASSWORD_QUEUE)
    public void receive(EmailEvent event) {

        String email = event.getEmail();
        String otp = event.getOtp();

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(email);

            helper.setSubject("🔐 SkyBooker Password Reset OTP");

            String htmlContent =
                    """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>

                            body{
                                font-family: Arial, sans-serif;
                                background:#f4f6f8;
                                padding:20px;
                            }

                            .container{
                                max-width:600px;
                                margin:auto;
                                background:white;
                                border-radius:15px;
                                overflow:hidden;
                                box-shadow:0 2px 10px rgba(0,0,0,0.1);
                            }

                            .header{
                                background:#0d6efd;
                                color:white;
                                padding:25px;
                                text-align:center;
                            }

                            .content{
                                padding:30px;
                                color:#333;
                            }

                            .otp-box{
                                margin:25px 0;
                                text-align:center;
                            }

                            .otp{
                                display:inline-block;
                                background:#f1f5ff;
                                color:#0d6efd;
                                font-size:32px;
                                font-weight:bold;
                                letter-spacing:8px;
                                padding:15px 30px;
                                border-radius:12px;
                            }

                            .footer{
                                margin-top:30px;
                                font-size:13px;
                                color:gray;
                                text-align:center;
                            }

                        </style>
                    </head>

                    <body>

                        <div class="container">

                            <div class="header">
                                <h1>✈️ SkyBooker</h1>
                                <h2>Password Reset Request</h2>
                            </div>

                            <div class="content">

                                <p>Hello Traveller 👋</p>

                                <p>
                                    We received a request to reset your password.
                                    Use the OTP below to continue.
                                </p>

                                <div class="otp-box">
                                    <div class="otp">
                                        %s
                                    </div>
                                </div>

                                <p>
                                    ⏳ This OTP is valid for
                                    <b>15 minutes</b>.
                                </p>

                                <p>
                                    If you did not request this,
                                    please ignore this email.
                                </p>

                                <div class="footer">
                                    This is an automated email from SkyBooker.
                                </div>

                            </div>

                        </div>

                    </body>
                    </html>
                    """.formatted(otp);

            helper.setText(htmlContent, true);

            mailSender.send(message);

            log.info("✅ OTP Email sent successfully to: " + email);

        } catch (Exception e) {

            log.info("❌ Failed to send email: " + e.getMessage());

        }
    }
}