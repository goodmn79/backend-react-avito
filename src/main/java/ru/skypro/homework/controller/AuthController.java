package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.user.Login;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.service.AuthService;


/**
 * Контроллер для управления регистрацией и авторизацией пользователя.
 * <p>
 * Обрабатывает HTTP-запросы для регистрации и авторизации пользователя.
 * </p>
 *
 * @author Powered by ©AYE.team, sazonovfm, skypro-backend
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Авторизация пользователя.
     * <br> Endpoint: POST /login
     *
     * @param login имя и пароль пользователя
     * @return Ответ с HTTP статусом 200 OK, если авторизация успешна, или 401 UNAUTHORIZED, если данные авторизации некорректны.
     */
    @Operation(tags = {"Авторизация"},
            summary = "Авторизация пользователя")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Invoke method 'login'");
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Регистрация пользователя.
     * <br> Endpoint: POST /register
     *
     * @param register объект с данными о пользователя для регистрации
     * @return Ответ с HTTP статусом 201 CREATED, если регистрация успешна, или 404 BAD_REQUEST, если данные регистрации некорректны.
     */
    @Operation(tags = {"Регистрация"},
            summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        log.info("Invoke method 'register'");
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
