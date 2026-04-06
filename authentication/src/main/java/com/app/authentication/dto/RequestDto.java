package com.app.authentication.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private String fullName;
    private String email;
    private String passwordHash;
    private String phone;
    private String role;
    private String provider;
    private Boolean isActive;
    private String passportNumber;
    private String nationality;
    private LocalDate createdAt;
}
