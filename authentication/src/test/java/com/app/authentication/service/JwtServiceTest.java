package com.app.authentication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken("test@gmail.com", "ROLE_USER");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken("test@gmail.com", "ROLE_USER");

        String username = jwtService.extractUsername(token);

        assertEquals("test@gmail.com", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtService.generateToken("test@gmail.com", "ROLE_USER");

        UserDetails userDetails = User.builder()
                .username("test@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtService.generateToken("test@gmail.com", "ROLE_USER");

        UserDetails userDetails = User.builder()
                .username("wrong@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        UserDetails userDetails = User.builder()
                .username("test@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        boolean isValid = jwtService.validateToken("invalid-token", userDetails);

        assertFalse(isValid);
    }

    @Test
    void testExtractAllClaims() {
        String token = jwtService.generateToken("test@gmail.com", "ROLE_ADMIN");

        String username = jwtService.extractAllClaims(token).getSubject();
        String role = (String) jwtService.extractAllClaims(token).get("roles");

        assertEquals("test@gmail.com", username);
        assertEquals("ROLE_ADMIN", role);
    }
}