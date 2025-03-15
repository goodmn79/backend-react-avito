package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.user.Login;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        objectMapper = new ObjectMapper();
        username = "email@example.com";
        password = "password";
    }

    @Test
    void loginSuccess() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn(true);

        mockMvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper
                                        .writeValueAsString(
                                                new Login()
                                                        .setUsername(username)
                                                        .setPassword(password))
                        ))
                .andExpect(status().isOk());

        verify(authService).login(eq(username), eq(password));
    }

    @Test
    void loginFailure() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn(false);

        mockMvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper
                                        .writeValueAsString(
                                                new Login()
                                                        .setUsername(username)
                                                        .setPassword(password))
                        ))
                .andExpect(status().isUnauthorized());

        verify(authService).login(eq(username), eq(password));
    }

    @Test
    void registerSuccess() throws Exception {
        when(authService.register(any())).thenReturn(true);

        mockMvc
                .perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper
                                        .writeValueAsString(
                                                new Register()
                                                        .setUsername(username)
                                                        .setPassword(password))
                        ))
                .andExpect(status().isCreated());

        verify(authService).register(any());
    }

    @Test
    void registerFailure() throws Exception {
        when(authService.register(any())).thenReturn(false);

        mockMvc
                .perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper
                                        .writeValueAsString(
                                                new Register()
                                                        .setUsername(username)
                                                        .setPassword(password))
                        ))
                .andExpect(status().isBadRequest());

        verify(authService).register(any());
    }
}