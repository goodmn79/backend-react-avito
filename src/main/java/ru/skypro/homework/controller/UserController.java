package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public void setPassword(@RequestBody NewPassword newPassword) {
    }

    @GetMapping("/me")
    public User getUser() {
        return new User();
    }

    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser) {
        return new UpdateUser();
    }

    @PatchMapping("/me/image")
    public void updateUserImage(@RequestParam("image")  MultipartFile image) throws IOException {
    }

}

