package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

/**
 * Реализация сервиса для работы с пользователем.
 * <br> Этот класс реализует интерфейс {@link UserService}
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    /**
     * Обновление пароля пользователя.
     *
     * @param newPassword новый пароль пользователя
     */
    @Override
    public void updatePassword(NewPassword newPassword) {
        UserEntity currentUser = this.getCurrentUser();
        log.warn("Changing the password of an authorized user.");
        this.checkPassword(newPassword.getCurrentPassword(), currentUser.getPassword());
        String updatedPassword =
                encoder.encode(
                        DataValidator.validatedData(newPassword.getNewPassword(), 8, 16)
                );
        userRepository.save(currentUser.setPassword(updatedPassword));
        log.info("The password of the authorized user has been successfully changed.");
    }

    /**
     * Получение данных авторизованного пользователя.
     *
     * @return объект {@link User}, содержащий данные пользователя
     */
    @Override
    public User getUser() {
        log.warn("Getting authorized user data.");
        User user = userMapper.map(this.getCurrentUser());
        log.info("The authorized user's data has been successfully received.");
        return user;
    }

    /**
     * Обновление данных авторизованного пользователя.
     *
     * @param updateUser данные пользователя
     * @return объект {@link UpdateUser}, содержащий обновленные данные пользователя
     */
    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        log.warn("Updating the authorized user's data.");
        userRepository.save(
                userMapper.map(updateUser, this.getCurrentUser())
        );
        log.info("The authorized user's data has been successfully updated.");
        return updateUser;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param register данные для регистрации
     */
    @Override
    public void addUser(Register register) {
        UserEntity user = userMapper.map(register);
        log.warn("Saving a new user in the database.");
        userRepository.save(user);
        log.info("The user has been saved successfully.");
    }

    /**
     * Обновление аватара пользователя.
     *
     * @param file файл нового аватара пользователя
     */
    @Override
    @Transactional
    public void updateOrCreateUserImage(MultipartFile file) {
        UserEntity user = this.getCurrentUser();
        Image userImage;
        if (user.getImage() == null) {
            log.warn("Saving the current user's avatar.");
            userImage = imageService.saveImage(file);
        } else {
            log.warn("Updating the current user's avatar: '{}'.", user.getImage().getPath());
            userImage = imageService.updateImage(file, user.getImage().getId());
        }
        userRepository.save(user.setImage(userImage));
        log.info("Avatar has been successfully updated.");
    }

    /**
     * Проверка существования пользователя.
     *
     * @param username имя пользователя
     * @return {@code true}, если пользователь с таким именем уже существует, иначе {@code false}
     */
    @Override
    public boolean userExists(String username) {
        boolean isExists = userRepository.existsByUsername(username);
        log.debug("Checking the existence of the current user, the result: '{}'", isExists);
        return isExists;
    }

    /**
     * Получение текущего пользователя.
     *
     * @return объект {@link UserEntity}, содержащий данные пользователя
     * @throws UserNotFoundException Если пользователь не найден
     */
    @Override
    public UserEntity getCurrentUser() {
        log.info("Getting the current user.");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    log.error("User '{}'not found.", username);
                    throw new UserNotFoundException();
                });
    }

    private void checkPassword(String password, String actualPassword) {
        if (!encoder.matches(password, actualPassword)) {
            log.error("The password to change in the request does not match the password of the current user.!");
            throw new PasswordDoesNotMatchException();
        }
    }
}
