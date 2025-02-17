package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Operation(
            tags = {"Пользователи"},
            summary = "Обновление пароля",
            operationId = "setPassword",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/NewPassword")
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword) {
    }

    @Operation(
            tags = {"Пользователи"},
            summary = "Получение информации об авторизованном пользователе",
            operationId = "getUser",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/User")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
    @GetMapping("/me")
    public User getUser() {
        return new User();
    }

    @Operation(
            tags = {"Пользователи"},
            summary = "Обновление информации об авторизованном пользователе",
            operationId = "updateUser",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(ref = "#/components/schemas/UpdateUser")
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/UpdateUser")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }

    )
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        return new UpdateUser();
    }

    @Operation(
            tags = {"Пользователи"},
            summary = "Обновление аватара авторизованного пользователя",
            operationId = "updateUserImage",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam("image") MultipartFile image) throws IOException {
    }

}

