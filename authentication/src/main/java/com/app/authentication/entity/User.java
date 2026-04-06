package com.app.authentication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long userId;
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
