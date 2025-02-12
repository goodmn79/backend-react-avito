package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody NewPassword newPassword) {
        return newPassword;
    }

    @GetMapping("/me")
    public User getCurrentUserInfo() {
        return new User();
    }

    @PatchMapping("/me")
    public UpdateUser updateUserInfo(@RequestBody UpdateUser updateUser) {
        return new UpdateUser();
    }

    @PatchMapping("/me/image")
    public String updateUserImage(@RequestBody String image) {
        return image;
    }

}

