package com.app.authentication.controller;

import com.app.authentication.config.UserDetail;
import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.security.JwtFilter;
import com.app.authentication.service.AuthService;
import com.app.authentication.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
@Import(JwtFilter.class)
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Controller dependency
    @MockitoBean
    private AuthService authService;

    // JwtFilter dependency
    @MockitoBean
    private JwtService jwtService;

    // JwtFilter dependency
    @MockitoBean
    private UserDetail userDetail;

    @Test
    @WithMockUser(username = "lucky", roles = {"USER"})
    void testUpdateProfile() throws Exception {

        ResponseUser user = new ResponseUser();
        user.setFullName("Lucky");
        user.setEmail("lucky@gmail.com");
        user.setNationality("Indian");
        user.setPassportNumber("P123456");
        
        RequestDto userDto = new RequestDto();
        userDto.setFullName("Lucky");
        userDto.setEmail("lucky@gmail.com");
        userDto.setNationality("Indian");
        userDto.setPassportNumber("P123456");

        when(authService.updateProfile(1L, userDto))
                .thenReturn(user);

        mockMvc.perform(
                put("/user/update-profile")
                        .param("id", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
        )
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "lucky", roles = {"USER"})
    void testDeactivateAccount() throws Exception {

        doNothing().when(authService).deactivateAccount(1L);

        mockMvc.perform(
                patch("/user/deactivate-account")
                        .param("id", "1")
                        .with(csrf())
        )
        .andExpect(status().isOk());
    }
}