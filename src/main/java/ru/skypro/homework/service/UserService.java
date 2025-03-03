package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.validation.Validatable;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.exception.WrongCurrentPasswordException;
import ru.skypro.homework.component.mapper.UserMapper;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final Validatable validator;


    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public void updatePassword(NewPassword newPassword, HttpServletResponse response, HttpServletRequest request) {
        log.warn("Изменение пароля авторизованного пользователя.");
        String currentPassword =
                validator.validatedData(newPassword.getCurrentPassword(), 8, 16);
        this.checkCurrentPassword(currentPassword);
        String updatedPassword =
                encoder.encode(
                        validator.validatedData(newPassword.getNewPassword(), 8, 16)
                );
        userRepository.save(getCurrentUser().setPassword(updatedPassword));
        this.clearSecurityContext(response, request);
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
        UserEntity user =
                this.getCurrentUser()
                        .setFirstName(
                                validator.validatedData(updateUser.getFirstName(), 2, 16))
                        .setLastName(
                                validator.validatedData(updateUser.getLastName(), 2, 16))
                        .setPhone(
                                validator.validatedData(updateUser.getPhone()));
        userRepository.save(user);
        log.info("Данные авторизованного пользователя успешно обновлены.");
        return updateUser;
    }

    public void addUser(Register register) {
        log.warn("Валидация данных нового пользователя.");
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

    public UserEntity getUserByUsername(String username) {
        log.warn("Поиск пользователя по логину...");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден!");
                    return new UserNotFoundException();
                });

    }

    public boolean userExists(String username) {
        boolean isExists = userRepository.existsByUsername(username);
        log.debug("Проверка существования текущего пользоввателя, результат: '{}'", isExists);
        return isExists;
    }

    public UserEntity getCurrentUser() {
        return userRepository.findByUsername(this.currentUserName())
                .orElseThrow(UserNotFoundException::new);
    }

    private String currentUserName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Логгин текущего пользоввателя: '{}'", username);
        return username;
    }

    private void checkCurrentPassword(String password) {
        if (!encoder.matches(password, getCurrentUser().getPassword())) {
            throw new WrongCurrentPasswordException("The wrong current password has been introduced");
        }
    }

    private void clearSecurityContext(HttpServletResponse response, HttpServletRequest request) {
        manager.deleteUser(this.currentUserName());
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Контекст безопасности очищен.");
        }
    }
}
