package com.app.authentication.controller;


import com.app.authentication.config.UserDetail;
import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.RequestToken;
import com.app.authentication.dto.ResponseDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.dto.SignInDto;
import com.app.authentication.service.AuthService;
import com.app.authentication.service.EmailService;
import com.app.authentication.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.authentication.repository.PasswordResetRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JwtController.class)
@AutoConfigureMockMvc(addFilters = false)
class JwtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PasswordResetRepo repo;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserDetail user;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestDto requestDto;
    private SignInDto signInDto;
    private ResponseUser responseUser;
    private ResponseDto responseDto;
    private RequestToken requestToken;

    @BeforeEach
    void setUp() {

        requestDto = new RequestDto();
        requestDto.setFullName("Lucky");
        requestDto.setEmail("lucky@gmail.com");
        requestDto.setPassword("123456");

        signInDto = new SignInDto();
        signInDto.setEmail("lucky@gmail.com");
        signInDto.setPassword("123456");

        responseUser = new ResponseUser();
        responseUser.setUserId(1L);
        responseUser.setFullName("Lucky");
        responseUser.setEmail("lucky@gmail.com");

        responseDto = new ResponseDto();
        responseDto.setToken("sample-jwt-token");

        requestToken = new RequestToken();
        requestToken.setEmail("lucky@gmail.com");
        requestToken.setRole("USER");
    }

    // =========================
    // SIGNUP TEST
    // =========================

    @Test
    void testSignup() throws Exception {

        when(authService.signup(Mockito.any(RequestDto.class)))
                .thenReturn("User Registered Successfully");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    // =========================
    // SIGNIN TEST
    // =========================

    @Test
    void testSignin() throws Exception {

        when(authService.signin(
                Mockito.eq("lucky@gmail.com"),
                Mockito.eq("123456")))
                .thenReturn(responseDto);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(status().isAccepted());
    }

    // =========================
    // FORGOT PASSWORD TEST
    // =========================

    @Test
    void testForgotPassword() throws Exception {

        mockMvc.perform(post("/auth/forgot-password")
                        .param("email", "lucky@gmail.com"))
                .andExpect(status().isOk());
    }

    // =========================
    // RESET PASSWORD TEST
    // =========================

    @Test
    void testResetPassword() throws Exception {

        when(emailService.resetPassword("123456", "newpass"))
                .thenReturn("Password Reset Successfully");

        mockMvc.perform(post("/auth/reset-password")
                        .param("otp", "123456")
                        .param("newPassword", "newpass"))
                .andExpect(status().isOk());
    }

    // =========================
    // GET PROFILE TEST
    // =========================

    @Test
    void testGetProfile() throws Exception {

        when(authService.getUserByEmail("lucky@gmail.com"))
                .thenReturn(responseUser);

        mockMvc.perform(get("/auth/getprofile")
                        .param("email", "lucky@gmail.com"))
                .andExpect(status().isFound());
    }

    // =========================
    // VALIDATE TOKEN TEST
    // =========================

    @Test
    void testValidatedToken() throws Exception {

        String token = "sampleToken";

        UserDetails userDetails = User.builder()
                .username("lucky@gmail.com")
                .password("123")
                .roles("USER")
                .build();

        when(jwtService.extractUsername(token))
                .thenReturn("lucky@gmail.com");

        when(user.loadUserByUsername("lucky@gmail.com"))
                .thenReturn(userDetails);

        when(jwtService.validateToken(token, userDetails))
                .thenReturn(true);

        mockMvc.perform(get("/auth/validated-token")
                        .param("token", token))
                .andExpect(status().isAccepted());
    }

    // =========================
    // GET DATA TEST
    // =========================

    @Test
    void testGetData() throws Exception {

        String token = "Bearer sampleToken";

        when(jwtService.extractUsername("sampleToken"))
                .thenReturn("lucky@gmail.com");

        when(authService.getUserByEmail("lucky@gmail.com"))
                .thenReturn(responseUser);

        mockMvc.perform(get("/auth/getdata")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    // =========================
    // GET TOKEN TEST
    // =========================

    @Test
    void testGetToken() throws Exception {

        RequestToken requestToken = new RequestToken();
        requestToken.setEmail("lucky@gmail.com");
        requestToken.setRole("USER");

        when(jwtService.generateToken("lucky@gmail.com", "USER"))
                .thenReturn("generated-token");

        mockMvc.perform(post("/auth/get-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestToken)))
                .andExpect(status().isCreated());
    }
}