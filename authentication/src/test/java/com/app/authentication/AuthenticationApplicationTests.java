package com.app.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.app.authentication.config.UserDetail;
import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.entity.User;
import com.app.authentication.exception.AuthenticationException;
import com.app.authentication.exception.ResouceNotFoundException;
import com.app.authentication.repository.UserRepository;
import com.app.authentication.service.AuthService;
import com.app.authentication.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthenticationApplicationTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserDetail userDetail;

    @Mock
    private AuthenticationManager manager;

   

    @Test
    void signup_userAlreadyExists() {
        RequestDto request = new RequestDto();
        request.setEmail("test@gmail.com");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        assertThrows(AuthenticationException.class, () -> {
            authService.signup(request);
        });
    }

   
    // ---------------- VALIDATE TOKEN ----------------
    @Test
    void validateToken_success() {
        String token = "abc";

        when(jwtService.extractUsername(token)).thenReturn("test@gmail.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetail.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        boolean result = authService.validateToken(token);

        assertTrue(result);
    }

    // ---------------- GET USER ----------------
    @Test
    void getUserById_success() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseUser response = authService.getUserById(1L);

        assertEquals("test@gmail.com", response.getEmail());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResouceNotFoundException.class, () -> {
            authService.getUserById(1L);
        });
    }

    // ---------------- UPDATE ----------------
    @Test
    void updateProfile_success() {
        User user = new User();
        user.setUserId(1L);

        RequestDto dto = new RequestDto();
        dto.setEmail("new@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseUser response = authService.updateProfile(1L, dto);

        assertEquals("new@gmail.com", response.getEmail());
        verify(userRepository).save(user);
    }

    // ---------------- PASSWORD ----------------
    @Test
    void changePassword_success() {
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encoder.encode("newPass")).thenReturn("encodedPass");

        authService.changePassword(1L, "newPass");

        assertEquals("encodedPass", user.getPasswordHash()); // IMPORTANT
        verify(userRepository).save(user);
    }

    // ---------------- DEACTIVATE ----------------
    @Test
    void deactivateAccount_success() {
        User user = new User();
        user.setUserId(1L);
        user.setIsActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        authService.deactivateAccount(1L);

        assertFalse(user.getIsActive());
        verify(userRepository).save(user);
    }

    // ---------------- GET ALL ----------------
    @Test
    void getAllUser_success() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@gmail.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<ResponseUser> result = authService.getAllUser();

        assertEquals(1, result.size());
        assertEquals("test@gmail.com", result.get(0).getEmail());
    }
    
    @Test
    void signin_invalidCredentials() {
        String email = "test@gmail.com";
        String password = "wrong";

        when(manager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        assertThrows(RuntimeException.class, () -> {
            authService.signin(email, password);
        });
    }
    
    @Test
    void validateToken_invalid() {
        String token = "invalid";

        when(jwtService.extractUsername(token)).thenReturn("test@gmail.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetail.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        boolean result = authService.validateToken(token);

        assertFalse(result);
    }
    
  
}