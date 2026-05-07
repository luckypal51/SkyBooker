package com.app.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEvent {
	
	@NotBlank(message = "Email can not be empty")
	@Email(message = "Please enter valid email")
    private String email;
	
	@Pattern(regexp = "^[0-9]{6}",message = "Invalid OTP")
    private String otp;

}