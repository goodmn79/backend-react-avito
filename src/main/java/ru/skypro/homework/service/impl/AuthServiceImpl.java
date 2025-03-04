package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public boolean login(String username, String password) {
        log.warn("Аутентификация пользователя...");

        if (manager.userExists(username)) {
            log.info("Пользователь уже аутентифицирован.");
            return true;
        }

        UserEntity userEntity = userService.getUserByUsername(username);
        log.info("Пользователь найден.");
        if (encoder.matches(password, userEntity.getPassword())) {
            manager.createUser(
                    User.builder()
                            .password(userEntity.getPassword())
                            .username(userEntity.getUsername())
                            .roles(userEntity.getRole().name())
                            .build());
            log.info("Успешная аутентификация пользователя.");
            return true;
        }
        log.error("Неудачная аутентификация пользователя.");
        return false;
    }

    @Override
    public boolean register(Register register) {
        log.info("Регистрация нового пользователя...");
        if (userService.userExists(register.getUsername())) {
            log.error("Пользователь уже существует!");
            return false;
        }
        userService.addUser(register);
        log.info("Регистрация пользователя успешно завершена.");
        return true;
    }

    @Override
    public void clearSecurityContext(HttpServletResponse response, HttpServletRequest request) {
        log.warn("Завершение сеанса!");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        manager.deleteUser(username);
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Контекст безопасности очищен.");
        }
    }
}
