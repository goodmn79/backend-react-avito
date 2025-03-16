package ru.skypro.homework.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.user.Register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserDetailServiceImpl userDetailService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AuthServiceImpl authService;

    private String username;
    private String password;
    private Register register;

    @BeforeEach
    void setUp() {
        username = "user";
        password = "password";

        register = new Register();
        register.setUsername("newUser");
        register.setPassword("password");
    }

    @Test
    void testLoginSuccess() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userService.userExists(anyString())).thenReturn(true);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(true);

        boolean result = authService.login(username, password);

        assertThat(result).isTrue();
        verify(userDetailService).loadUserByUsername(username);
        verify(encoder).matches(password, userDetails.getPassword());
    }

    @Test
    void testLoginFailure() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userService.userExists(anyString())).thenReturn(true);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(false);

        boolean result = authService.login(username, password);

        assertThat(result).isFalse();
        verify(userDetailService).loadUserByUsername(username);
        verify(encoder).matches(password, userDetails.getPassword());
    }

    @Test
    void testRegisterSuccess() {
        when(userService.userExists(register.getUsername())).thenReturn(false);

        boolean result = authService.register(register);

        assertThat(result).isTrue();
        verify(userService).userExists(register.getUsername());
        verify(userService).addUser(register);
    }

    @Test
    void testRegisterFailure() {
        when(userService.userExists(register.getUsername())).thenReturn(true);

        boolean result = authService.register(register);

        assertThat(result).isFalse();
        verify(userService).userExists(register.getUsername());
        verify(userService, never()).addUser(register);
    }

    @Test
    void testClearSecurityContext() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authService.clearSecurityContext(response, request);

        verify(session, times(2)).invalidate();
        verify(request, times(2)).getSession(false);
        verify(request, times(1)).getSession();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}