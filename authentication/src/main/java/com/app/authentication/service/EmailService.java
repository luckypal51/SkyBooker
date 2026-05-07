package com.app.authentication.service;

import com.app.authentication.entity.PasswordResetToken;
import com.app.authentication.entity.User;
import com.app.authentication.repository.PasswordResetRepo;
import com.app.authentication.repository.UserRepository;
import com.app.authentication.util.ConstantValue;

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
    ForgotPasswordProducer forgotPasswordProducer;

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordResetRepo repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String forgotPassword(String email, String otp) {

        Optional<PasswordResetToken> existing = repo.findByToken(otp);

        if (existing.isPresent() &&
            existing.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException(ConstantValue.OTP_ALREADY_SENT);
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setToken(otp);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        repo.save(token);

        forgotPasswordProducer.sendEmailEvent(email, otp);

        return ConstantValue.OTP_SENT;
    }

    public String resetPassword(String otp, String newPassword) {

        PasswordResetToken resetToken = repo.findByToken(otp)
                .orElseThrow(() -> new RuntimeException(ConstantValue.INVALID_OTP));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(ConstantValue.OTP_EXPIRE);
        }

        User user = userRepo.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException(ConstantValue.USER_NOT_FOUND));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        repo.delete(resetToken);

        return ConstantValue.PASSWORD_RESET_SUCCESSFULLY;
    }
}