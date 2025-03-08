package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder encoder;
    private final UserServiceImpl userServiceImpl;

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public boolean login(String username, String password) {
        log.warn("User authentication...");

        if (userServiceImpl.userExists(username)) {
            UserDetails user = userDetailService.loadUserByUsername(username);
            log.info("The user has been found.");

            if (encoder.matches(password, user.getPassword())) {
                log.info("Successful user authentication.");
                return true;
            }
        }

        log.error("Failed user authentication.");
        return false;
    }

    @Override
    public boolean register(Register register) {
        log.info("Registering a new user...");
        if (userServiceImpl.userExists(register.getUsername())) {
            log.error("The user already exists!");
            return false;
        }
        userServiceImpl.addUser(register);
        log.info("User registration completed successfully.");
        return true;
    }

    @Override
    public void clearSecurityContext(HttpServletResponse response, HttpServletRequest request) {
        log.warn("Session Termination!");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("The security context is cleared.");
        }
    }
}
