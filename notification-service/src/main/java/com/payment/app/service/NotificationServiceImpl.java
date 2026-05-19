package com.payment.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.payment.app.entity.Notification;
import com.payment.app.repository.NotificationRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(Notification notification) {
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);
        repo.save(notification);
    }

    @Override
    public void sendBookingConfirmation(Long userId,
                                        Long bookingId,
                                        String email,
                                        String phone) {

        String pnr = "SB" + bookingId;

        // ================= APP NOTIFICATION =================

        Notification notif = Notification.builder()
                .recipientId(userId)
                .type("BOOKING_CONFIRMED")
                .title("✈️ Booking Confirmed Successfully!")
                .message(
                        "Your flight booking has been confirmed.\n" +
                        "🎫 PNR: " + pnr + "\n" +
                        "🛫 We wish you a pleasant journey with SkyBooker."
                )
                .channel("APP")
                .relatedBookingId(bookingId)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();

        repo.save(notif);

        // ================= EMAIL =================

        String emailBody =
                """
                <html>
                <body style='font-family:Arial,sans-serif;
                             background:#f4f6f8;
                             padding:20px;'>

                    <div style='max-width:600px;
                                margin:auto;
                                background:white;
                                border-radius:12px;
                                overflow:hidden;
                                box-shadow:0 2px 10px rgba(0,0,0,0.1);'>

                        <div style='background:#0d6efd;
                                    color:white;
                                    padding:20px;
                                    text-align:center;'>

                            <h1>✈️ SkyBooker</h1>
                            <h2>Booking Confirmed</h2>
                        </div>

                        <div style='padding:25px;'>

                            <h3>Hello Traveller 👋</h3>

                            <p>
                                Your booking has been successfully confirmed.
                            </p>

                            <div style='background:#f1f5ff;
                                        padding:15px;
                                        border-radius:10px;
                                        margin:20px 0;'>

                                <h2 style='margin:0;color:#0d6efd;'>
                                    🎫 PNR: %s
                                </h2>

                            </div>

                            <p>
                                Thank you for choosing <b>SkyBooker</b>.
                                We wish you a safe and pleasant journey ✈️
                            </p>

                            <br>

                            <p style='color:gray;font-size:14px;'>
                                This is an auto-generated email from SkyBooker.
                            </p>

                        </div>

                    </div>

                </body>
                </html>
                """.formatted(pnr);

        sendEmail(email,
                "✈️ SkyBooker - Booking Confirmed",
                emailBody);

        // ================= SMS =================

        String sms =
                "SkyBooker ✈️\n" +
                "Your booking is confirmed.\n" +
                "PNR: " + pnr + "\n" +
                "Have a safe journey!";

        sendSMS(phone, sms);
    }
    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return repo.findByRecipientId(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification n = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setIsRead(true);
        repo.save(n);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return repo.countByRecipientIdAndIsRead(userId, false);
    }

    private void sendEmail(String to, String subject, String body) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // true = HTML email
            helper.setText(body, true);

            mailSender.send(message);

            System.out.println("✅ Email sent successfully to " + to);

        } catch (Exception e) {

            System.out.println("❌ Email failed: " + e.getMessage());

        }
    }

    private void sendSMS(String phone, String message) {
        System.out.println("SMS sent to " + phone + ": " + message);
    }
}
