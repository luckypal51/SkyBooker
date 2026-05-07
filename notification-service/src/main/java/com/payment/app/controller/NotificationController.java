package com.payment.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.app.dto.BookingConfirmDto;
import com.payment.app.entity.Notification;
import com.payment.app.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    
    
    @PostMapping("/confirm-booking")
    public ResponseEntity<?> confirmBookingEmail(@RequestBody BookingConfirmDto bookingConfirmDto){
    	service.sendBookingConfirmation(bookingConfirmDto.getUserId(),bookingConfirmDto.getBookingId(),bookingConfirmDto.getEmail(),bookingConfirmDto.getPhone());
    	
    	return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return service.getUserNotifications(userId);
    }

    @PutMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return "Marked as read";
    }

    @GetMapping("/unread/{userId}")
    public long getUnreadCount(@PathVariable Long userId) {
        return service.getUnreadCount(userId);
    }
}