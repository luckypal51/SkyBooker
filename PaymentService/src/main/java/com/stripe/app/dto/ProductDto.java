package com.stripe.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long userId;
    private Long bookingId;
    private String productName;
    private double amount;
    private int quantity;
    private String currency;
}
