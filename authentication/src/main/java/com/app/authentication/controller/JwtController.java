package com.app.authentication.controller;

import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.dto.SignInDto;
import com.app.authentication.entity.PasswordResetToken;
import com.app.authentication.entity.User;
import com.app.authentication.repository.PasswordResetRepo;
import com.app.authentication.service.AuthService;
import com.app.authentication.service.EmailService;
import com.app.authentication.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class JwtController {

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordResetRepo repo;

    @Autowired
    EmailService emailService;

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RequestDto requestDto){
        System.out.println(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(requestDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseDto> signin(@RequestBody SignInDto signInDto){
        System.out.println(signInDto.getPassword());
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body(authService.signin(signInDto.getEmail(),signInDto.getPassword()));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String token = String.valueOf(new Random().nextInt(100000,999999));
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        repo.save(resetToken);
        String link = "OTP : " + token;
        emailService.send(email, link);
        return ResponseEntity.ok("OTP Sent");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp,
                                @RequestParam String newPassword) {
        return ResponseEntity.ok(emailService.resetPassword(otp,newPassword));
    }
    @GetMapping("/getprofile")
    public ResponseEntity<ResponseUser> getProfile(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(authService.getUserById(id));
    }
}
