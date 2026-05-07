package com.payment.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.payment.app.entity.Notification;
import com.payment.app.repository.NotificationRepository;

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
    public void sendBookingConfirmation(Long userId, Long bookingId, String email, String phone) {

       
        Notification notif = Notification.builder()
                .recipientId(userId)
                .type("BOOKING_CONFIRMED")
                .title("Booking Confirmed ✈️")
                .message("Your booking is confirmed. PNR: " + bookingId)
                .channel("APP")
                .relatedBookingId(bookingId)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();

        repo.save(notif);

       
        sendEmail(email, "Booking Confirmed",
                "Your booking is confirmed. PNR: " + bookingId);

        
        sendSMS(phone, "Booking Confirmed. PNR: " + bookingId);
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
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            mailSender.send(mail);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    private void sendSMS(String phone, String message) {
        System.out.println("SMS sent to " + phone + ": " + message);
    }
}
