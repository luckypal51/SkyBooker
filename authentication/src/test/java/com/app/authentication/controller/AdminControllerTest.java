package com.app.authentication.controller;

import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                com.app.authentication.security.JwtFilter.class,
                                com.app.authentication.config.UserDetail.class
                        }
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseUser responseUser;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        responseUser = new ResponseUser();
        responseUser.setUserId(1L);
        responseUser.setFullName("Lucky");
        responseUser.setEmail("lucky@gmail.com");

        requestDto = new RequestDto();
        requestDto.setFullName("Updated Lucky");
        requestDto.setEmail("updated@gmail.com");
    }

    @Test
    void testGetAllUser() throws Exception {
        List<ResponseUser> users = Arrays.asList(responseUser);

        when(authService.getAllUser()).thenReturn(users);

        mockMvc.perform(get("/admin/get-all-user"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserById() throws Exception {
        doNothing().when(authService).deleteUserById(1L);

        mockMvc.perform(delete("/admin/delete-user")
                        .param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateProfile() throws Exception {
        when(authService.updateProfile(
                Mockito.eq(1L),
                Mockito.any(RequestDto.class)))
                .thenReturn(responseUser);

        mockMvc.perform(put("/admin/update-profile")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }
}