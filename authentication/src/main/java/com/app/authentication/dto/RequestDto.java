package com.app.authentication.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be in email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
        message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Invalid Indian phone number"
    )
    private String phone;

    @NotBlank(message = "Role is required")
    private String role;

    private String provider;

    private Boolean active;

    @Pattern(
        regexp = "^[A-Z0-9]{6,9}$",
        message = "Invalid passport number"
    )
    private String passportNumber;

    @NotBlank(message = "Nationality is required")
    private String nationality;

   
}