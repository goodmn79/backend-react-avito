package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.UserEntity;

import java.io.IOException;


public interface UserService {
    void updatePassword(NewPassword newPassword);

    User getUser();

    UpdateUser updateUser(UpdateUser updateUser);

    void addUser(Register register);

    void updateUserImage(MultipartFile file) throws IOException;

    boolean userExists(String username);

    UserEntity getCurrentUser();
}
