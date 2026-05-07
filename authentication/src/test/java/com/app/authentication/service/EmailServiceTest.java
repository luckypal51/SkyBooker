package com.app.authentication.service;

import com.app.authentication.entity.PasswordResetToken;
import com.app.authentication.entity.User;
import com.app.authentication.repository.PasswordResetRepo;
import com.app.authentication.repository.UserRepository;
import com.app.authentication.util.ConstantValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private ForgotPasswordProducer forgotPasswordProducer;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordResetRepo repo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmailService emailService;

    private PasswordResetToken token;
    private User user;

    @BeforeEach
    void setUp() {

        token = new PasswordResetToken();
        token.setId(1L);
        token.setEmail("lucky@gmail.com");
        token.setToken("123456");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        user = new User();
        user.setUserId(1L);
        user.setEmail("lucky@gmail.com");
        user.setPasswordHash("oldPassword");
    }

    /*
     =========================
     forgotPassword() TESTS
     =========================
     */

    @Test
    void testForgotPasswordSuccess() {

        when(repo.findByToken("123456"))
                .thenReturn(Optional.empty());

        String result = emailService.forgotPassword(
                "lucky@gmail.com",
                "123456"
        );

        assertEquals(ConstantValue.OTP_SENT, result);

        verify(repo, times(1)).save(any(PasswordResetToken.class));
        verify(forgotPasswordProducer, times(1))
                .sendEmailEvent("lucky@gmail.com", "123456");
    }

    @Test
    void testForgotPasswordOtpAlreadySent() {

        when(repo.findByToken("123456"))
                .thenReturn(Optional.of(token));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> emailService.forgotPassword(
                        "lucky@gmail.com",
                        "123456"
                )
        );

        assertEquals(
                ConstantValue.OTP_ALREADY_SENT,
                exception.getMessage()
        );

        verify(repo, never()).save(any());
        verify(forgotPasswordProducer, never())
                .sendEmailEvent(anyString(), anyString());
    }

    /*
     =========================
     resetPassword() TESTS
     =========================
     */

    @Test
    void testResetPasswordSuccess() {

        when(repo.findByToken("123456"))
                .thenReturn(Optional.of(token));

        when(userRepo.findByEmail("lucky@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedPassword");

        String result = emailService.resetPassword(
                "123456",
                "newPassword"
        );

        assertEquals(
                ConstantValue.PASSWORD_RESET_SUCCESSFULLY,
                result
        );

        verify(userRepo, times(1)).save(user);
        verify(repo, times(1)).delete(token);
    }

    @Test
    void testResetPasswordInvalidOtp() {

        when(repo.findByToken("123456"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> emailService.resetPassword(
                        "123456",
                        "newPassword"
                )
        );

        assertEquals(
                ConstantValue.INVALID_OTP,
                exception.getMessage()
        );
    }

    @Test
    void testResetPasswordOtpExpired() {

        token.setExpiryDate(LocalDateTime.now().minusMinutes(5));

        when(repo.findByToken("123456"))
                .thenReturn(Optional.of(token));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> emailService.resetPassword(
                        "123456",
                        "newPassword"
                )
        );

        assertEquals(
                ConstantValue.OTP_EXPIRE,
                exception.getMessage()
        );
    }

    @Test
    void testResetPasswordUserNotFound() {

        when(repo.findByToken("123456"))
                .thenReturn(Optional.of(token));

        when(userRepo.findByEmail("lucky@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> emailService.resetPassword(
                        "123456",
                        "newPassword"
                )
        );

        assertEquals(
                ConstantValue.USER_NOT_FOUND,
                exception.getMessage()
        );
    }
}