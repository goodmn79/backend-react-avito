package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {

    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword) {
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public User getUser() {
        return new User();
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        return new UpdateUser();
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(content = @Content(mediaType = "multipart/form-data")))
    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam("image") MultipartFile image) throws IOException {
    }

}

