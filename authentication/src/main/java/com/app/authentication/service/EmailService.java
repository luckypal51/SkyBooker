package com.app.authentication.service;

import com.app.authentication.entity.PasswordResetToken;
import com.app.authentication.entity.User;
import com.app.authentication.repository.PasswordResetRepo;
import com.app.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordResetRepo repo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void send(String toEmail, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");

        message.setText(
                "Otp is given below to reset your password:\n" +
                        resetLink +
                        "\nThis OTP will expire in 15 minutes."
        );

        mailSender.send(message);
    }

    public String resetPassword(String otp,String newPassword){
        PasswordResetToken resetToken = repo.findByToken(otp)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        Optional<User> user = userRepo.findByEmail(resetToken.getEmail());

        user.get().setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user.get());
       return "Password Reset Completed";
    }


}