package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.UserEntity;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder encoder;

    public User map(UserEntity user) {
        return new User()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhone(user.getPhone())
                .setRole(user.getRole())
                .setImage(this.getUserImage(user));
    }

    public UserEntity map(UpdateUser updateUser, UserEntity user) {
        return user
                .setFirstName(
                        DataValidator.validatedData(updateUser.getFirstName(), 2, 16))
                .setLastName(
                        DataValidator.validatedData(updateUser.getLastName(), 2, 16))
                .setPhone(
                        DataValidator.validatedPhoneNumber(updateUser.getPhone()));
    }

    public UserEntity map(Register register) {
        return new UserEntity()
                .setUsername(
                        DataValidator.validatedData(register.getUsername(), 4, 32)
                                .toLowerCase())
                .setPassword(
                        encoder.encode(
                                DataValidator.validatedData(register.getPassword(), 8, 16)
                        ))
                .setFirstName(
                        DataValidator.validatedData(register.getFirstName(), 2, 16))
                .setLastName(
                        DataValidator.validatedData(register.getLastName(), 2, 16))
                .setPhone(
                        DataValidator.validatedPhoneNumber(register.getPhone()))
                .setEmail(register.getUsername())
                .setRole(register.getRole());
    }

    private String getUserImage(UserEntity user) {
        try {
            return user.getImage().getPath();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
