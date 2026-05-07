package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingConfirmDto {
    private Long userId;
    private Long bookingId;
    private String email;
    private String phone;
}
