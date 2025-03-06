package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.UserEntity;

import java.io.IOException;

/**
 * Сервис для работы с пользователем.
 * <br> Этот интерфейс предоставляет методы для операций с пользователем, таких как их обновление и получение.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public interface UserService {
    void updatePassword(NewPassword newPassword);

    User getUser();

    UpdateUser updateUser(UpdateUser updateUser);

    void addUser(Register register);

    void updateUserImage(MultipartFile file) throws IOException;

    boolean userExists(String username);

    UserEntity getCurrentUser();
}
