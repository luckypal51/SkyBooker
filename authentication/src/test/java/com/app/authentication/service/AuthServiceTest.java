package com.app.authentication.service;

import com.app.authentication.config.UserDetail;
import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.exception.AuthenticationException;
import com.app.authentication.exception.ResouceNotFoundException;
import com.app.authentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

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

    @InjectMocks
    private AuthService authService;

    private RequestDto requestDto;
    private com.app.authentication.entity.User appUser;

    @BeforeEach
    void setUp() {
        requestDto = new RequestDto();
        requestDto.setFullName("Lucky");
        requestDto.setEmail("lucky@gmail.com");
        requestDto.setPassword("12345");
        requestDto.setPhone("9876543210");
        requestDto.setRole("ROLE_USER");
        requestDto.setProvider("LOCAL");
        requestDto.setActive(true);
        requestDto.setPassportNumber("P12345");
        requestDto.setNationality("Indian");

        appUser = new com.app.authentication.entity.User();
        appUser.setUserId(1L);
        appUser.setFullName("Lucky");
        appUser.setEmail("lucky@gmail.com");
        appUser.setPasswordHash("encodedPassword");
        appUser.setPhone("9876543210");
        appUser.setRole("ROLE_USER");
        appUser.setProvider("LOCAL");
        appUser.setIsActive(true);
        appUser.setPassportNumber("P12345");
        appUser.setNationality("Indian");
        appUser.setCreatedAt(LocalDate.now());
    }

    @Test
    void testSignupSuccess() {
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(encoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        String result = authService.signup(requestDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSignupUserAlreadyExists() {
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        assertThrows(AuthenticationException.class,
                () -> authService.signup(requestDto));
    }

    @Test
    void testSigninSuccess() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "lucky@gmail.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(userDetail.loadUserByUsername("lucky@gmail.com"))
                .thenReturn(userDetails);

        when(jwtService.generateToken("lucky@gmail.com", "ROLE_USER"))
                .thenReturn("jwt-token");

        ResponseDto response = authService.signin("lucky@gmail.com", "12345");

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void testGetUserByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(appUser));

        ResponseUser response = authService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Lucky", response.getFullName());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResouceNotFoundException.class,
                () -> authService.getUserById(1L));
    }

    @Test
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail("lucky@gmail.com"))
                .thenReturn(Optional.of(appUser));

        ResponseUser response = authService.getUserByEmail("lucky@gmail.com");

        assertNotNull(response);
        assertEquals("lucky@gmail.com", response.getEmail());
    }

    @Test
    void testChangePassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(appUser));
        when(encoder.encode("newPassword")).thenReturn("encodedNewPassword");

        authService.changePassword(1L, "newPassword");

        verify(userRepository, times(1)).save(appUser);
    }

    @Test
    void testDeactivateAccount() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(appUser));

        authService.deactivateAccount(1L);

        assertFalse(appUser.getIsActive());
        verify(userRepository, times(1)).save(appUser);
    }

    @Test
    void testGetAllUser() {
        when(userRepository.findAll()).thenReturn(List.of(appUser));

        List<ResponseUser> result = authService.getAllUser();

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteUserById() {
        authService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}