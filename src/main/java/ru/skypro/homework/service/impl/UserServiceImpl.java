package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageServiceImpl imageServiceImpl;

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

    public User getUser() {
        log.warn("Getting authorized user data.");
        User user = userMapper.map(this.getCurrentUser());
        log.info("The authorized user's data has been successfully received.");
        return user;
    }

    public UpdateUser updateUser(UpdateUser updateUser) {
        log.warn("Updating the authorized user's data.");
        userRepository.save(
                userMapper.map(updateUser, this.getCurrentUser())
        );
        log.info("The authorized user's data has been successfully updated.");
        return updateUser;
    }

    public void addUser(Register register) {
        UserEntity user = userMapper.map(register);
        log.warn("Saving a new user in the database.");
        userRepository.save(user);
        log.info("The user has been saved successfully.");
    }

    @Transactional
    public Image updateOrCreateUserImage(MultipartFile file) {
        log.warn("Updating the current user's avatar.");
        UserEntity user = this.getCurrentUser();
        Image userImage =
                user.getImage() == null ?
                        imageServiceImpl.saveImage(file) :
                        imageServiceImpl.updateImage(file, user.getImage().getId());

        UserEntity updatedUser = userRepository.save(user.setImage(userImage));
        log.info("Avatar has been successfully updated.");
        return updatedUser.getImage();
    }

    public boolean userExists(String username) {
        boolean isExists = userRepository.existsByUsername(username);
        log.debug("Checking the existence of the current user, the result: '{}'", isExists);
        return isExists;
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private void checkPassword(String password, String actualPassword) {
        if (!encoder.matches(password, actualPassword)) {
            log.error("The password to change in the request does not match the password of the current user.!");
            throw new PasswordDoesNotMatchException();
        }
    }
}
