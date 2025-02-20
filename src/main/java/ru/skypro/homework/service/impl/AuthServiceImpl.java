package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public boolean login(String userName, String password) {
        log.warn("Аутентификация пользователя...");
        if (manager.userExists(userName)) {
            UserDetails user = manager.loadUserByUsername(userName);
            boolean matches = encoder.matches(password, user.getPassword());
            log.info("Проверка аутентификации пользователя в текущей сессии, результат: '{}'", matches);
            return matches;
        }
        UserEntity userEntity = userService.getUserByUsername(userName);
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

}
