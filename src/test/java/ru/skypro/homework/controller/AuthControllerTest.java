package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.UserDetailServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(WebSecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity()
                .setUsername("testUser")
                .setPassword("password");
    }

    @Test
    void loginSuccess() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

        verify(authService).login(userEntity.getUsername(), userEntity.getPassword());
    }

    @Test
    void loginFailure() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized());

        verify(authService).login(userEntity.getUsername(), userEntity.getPassword());
    }

    @Test
    void registerSuccess() throws Exception {
        when(authService.register(any())).thenReturn(true);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isCreated());

        verify(authService).register(any());
    }

    @Test
    void registerFailure() throws Exception {
        when(authService.register(any())).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest());

        verify(authService).register(any());
    }
}