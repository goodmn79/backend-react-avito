package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.Validatable;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.model.UserEntity;

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
                        validator.validate(register.getUsername(), 4, 32)
                                .toLowerCase())
                .setPassword(
                        encoder.encode(
                                validator.validate(register.getPassword(), 8, 16)
                        ))
                .setFirstName(
                        validator.validate(register.getFirstName(), 2, 16))
                .setLastName(
                        validator.validate(register.getLastName(), 2, 16))
                .setPhone(
                        validator.validate(register.getPhone()))
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
