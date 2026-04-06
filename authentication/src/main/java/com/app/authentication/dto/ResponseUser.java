package com.app.authentication.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    @Id
    @GeneratedValue
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String provider;
    private Boolean isActive;
    private String passportNumber;
    private String nationality;
    private LocalDate createdAt;

}
