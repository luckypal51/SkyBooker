package com.stripe.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
      @Id
      private String paymentId;
      private Long bookingId;
      private Long userId;
      private Double amount;
      private String currency;
      private String status;
      private String transactionId;
      private LocalDateTime paidAt;
      private LocalDateTime refundedAt;
      private Double refundedAmount;

}
