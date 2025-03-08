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
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
@CrossOrigin(origins = "http://localhost:3000")
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
     * @param response    объект {@link HttpServletResponse}
     * @param request     объект {@link HttpServletRequest}
     */
    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        log.info("Invoke method 'setPassword'");
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
        log.info("Invoke method 'getUser'");
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
        log.info("Invoke method 'updateUser'");
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
    public void updateUserImage(@RequestBody MultipartFile image, HttpServletResponse response) {
        log.info("Invoke method 'updateUserImage'");
        Image userImage = userService.updateOrCreateUserImage(image);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(userImage.getMediaType());
        response.setContentLength(userImage.getSize());
        Path path = Path.of(userImage.getPath());
        log.info("Image path: {}", path);
        try (OutputStream out = response.getOutputStream()) {
            Files.copy(path, out);
        } catch (Exception e) {
            log.error("{}. Image upload error!", e.getMessage());
        }
    }
}

