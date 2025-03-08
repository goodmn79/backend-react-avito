package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Контроллер для работы с пользователями.
 * <p>
 * Обрабатывает HTTP-запросы для получения и обновления информации о пользователе.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    /**
     * Обновление пароля пользователя.
     * <br>Endpoint: POST /users/set_password
     *
     * @param newPassword объект с новым паролем
     * @param response объект {@link HttpServletResponse}
     * @param request объект {@link HttpServletRequest}
     */
    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        log.info("Вызван метод 'setPassword'");
        userService.updatePassword(newPassword);
        authService.clearSecurityContext(response, request);
    }

    /**
     * Получение информации об авторизованном пользователе.
     * <br>Endpoint: GET /users/me
     *
     * @return объект {@link User} с информацией о пользователе
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public User getUser() {
        log.info("Вызван метод 'getUser'");
        return userService.getUser();
    }

    /**
     * Обновление информации об авторизованном пользователе.
     * <br>Endpoint: PATCH /users/me
     *
     * @param updateUser объект с обновленной информацией о пользователе
     * @return обновленный объект {@link User}
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        log.info("Вызван метод 'updateUser'");
        return userService.updateUser(updateUser);
    }

    /**
     * Обновление аватара авторизованного пользователя.
     * <br>Endpoint: PATCH /users/me/image
     *
     * @param image файл изображения
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(content = @Content(mediaType = "multipart/form-data")))
    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam("image") MultipartFile image) throws IOException {
        log.info("Вызван метод 'updateUserImage'");
        userService.updateUserImage(image);
    }
}

