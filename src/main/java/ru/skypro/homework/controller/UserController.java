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
import ru.skypro.homework.exception.UnsuccessfulImageSavingException;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        log.info("Вызван метод 'setPassword'");
        userService.updatePassword(newPassword);
        authService.clearSecurityContext(response, request);
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public User getUser() {
        log.info("Вызван метод 'getUser'");
        return userService.getUser();
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        log.info("Вызван метод 'updateUser'");
        return userService.updateUser(updateUser);
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(content = @Content(mediaType = "multipart/form-data")))
    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam("image") MultipartFile image, HttpServletResponse response) {
        log.info("Вызван метод 'updateUserImage'");
        Image userImage = userService.updateUserImage(image);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(userImage.getMediaType());
        response.setContentLength(userImage.getSize());
        try (OutputStream out = response.getOutputStream()) {
            Files.copy(Path.of(userImage.getPath()), out);
        } catch (Exception e) {
            log.error("{}. Ошибка загрузки изображения!", e.getMessage());
            throw new UnsuccessfulImageSavingException();
        }
    }
}

