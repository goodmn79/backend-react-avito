package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.mapper.UserMapper;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.PasswordDoesNotMatchException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public void updatePassword(NewPassword newPassword) {
        UserEntity currentUser = this.getCurrentUser();
        log.warn("Изменение пароля авторизованного пользователя.");
        this.checkPassword(newPassword.getCurrentPassword(), currentUser.getPassword());
        String updatedPassword =
                encoder.encode(
                        DataValidator.validatedData(newPassword.getNewPassword(), 8, 16)
                );
        userRepository.save(currentUser.setPassword(updatedPassword));
        log.info("Пароль авторизованного пользователя успешно изменён.");
    }

    public User getUser() {
        log.warn("Получение данных авторизованного пользователя.");
        User user = userMapper.map(this.getCurrentUser());
        log.info("Данные авторизованного пользователя успешно получены.");
        return user;
    }

    public UpdateUser updateUser(UpdateUser updateUser) {
        log.warn("Обновление данных авторизованного пользователя.");
        userRepository.save(
                userMapper.map(updateUser, this.getCurrentUser())
        );
        log.info("Данные авторизованного пользователя успешно обновлены.");
        return updateUser;
    }

    public void addUser(Register register) {
        UserEntity user = userMapper.map(register);
        log.warn("Сохранение нового пользователя в базе данных.");
        userRepository.save(user);
        log.info("Пользователь успешно сохранён.");
    }

    public void updateUserImage(MultipartFile file) throws IOException {
        log.warn("Обновление аватара текущего пользователя.");
        UserEntity user = this.getCurrentUser();
        Image userImage = imageService.saveImage(file, user.getId());
        userRepository.save(user.setImage(userImage));
        log.info("Аватар успешно обновлён.");
    }

    public boolean userExists(String username) {
        boolean isExists = userRepository.existsByUsername(username);
        log.debug("Проверка существования текущего пользователя, результат: '{}'", isExists);
        return isExists;
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private void checkPassword(String password, String actualPassword) {
        if (!encoder.matches(password, actualPassword)) {
            log.error("Пароль для изменения в запросе не совпадает с паролем текущего пользователя!");
            throw new PasswordDoesNotMatchException();
        }
    }
}

