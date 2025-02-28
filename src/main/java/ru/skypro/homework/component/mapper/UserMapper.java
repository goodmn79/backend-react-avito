package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.Validatable;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.UserEntity;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final Validatable validator;
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

    public UserEntity map(Register register) {
        return new UserEntity()
                .setUsername(
                        validator.validatedData(register.getUsername(), 4, 32)
                                .toLowerCase())
                .setPassword(
                        encoder.encode(
                                validator.validatedData(register.getPassword(), 8, 16)
                        ))
                .setFirstName(
                        validator.validatedData(register.getFirstName(), 2, 16))
                .setLastName(
                        validator.validatedData(register.getLastName(), 2, 16))
                .setPhone(
                        validator.validatedData(register.getPhone()))
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
