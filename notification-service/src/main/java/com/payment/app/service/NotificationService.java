package com.payment.app.service;

import java.util.List;

import com.payment.app.entity.Notification;

public interface NotificationService {

    void sendNotification(Notification notification);

    void sendBookingConfirmation(Long userId, Long bookingId, String email, String phone);

    List<Notification> getUserNotifications(Long userId);

    void markAsRead(Long notificationId);

    long getUnreadCount(Long userId);
}