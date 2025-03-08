package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.mapper.UserMapper;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.PasswordDoesNotMatchException;
import ru.skypro.homework.exception.ErrorImageProcessingException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageServiceImpl imageServiceImpl;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new UserEntity()
                .setId(1)
                .setUsername("username")
                .setEmail("test@test.com")
                .setPassword("password")
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setPhone("555")
                .setImage(mock(Image.class));
    }

    @Test
    void testAddUser_shouldSaveUser() {
        Register register = mock(Register.class);
        when(userMapper.map(register)).thenReturn(testEntity);
        when(userRepository.save(testEntity)).thenReturn(testEntity);

        userServiceImpl.addUser(register);

        verify(userRepository, times(1)).save(testEntity);
        verify(userMapper, times(1)).map(register);
    }

    @Test
    void testUserExists_whenUserExist_shouldReturnTrue() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        boolean actual = userServiceImpl.userExists(testEntity.getUsername());

        verify(userRepository, times(1))
                .existsByUsername(testEntity.getUsername());

        assertThat(actual).isTrue();
    }

    @Test
    void testUserExists_whenUserNotExist_shouldReturnFalse() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        boolean actual = userServiceImpl.userExists(testEntity.getUsername());

        verify(userRepository, times(1))
                .existsByUsername(testEntity.getUsername());

        assertThat(actual).isFalse();
    }


    @Nested
    class UserServiceImplTestWithSecurityContext {
        @BeforeEach
        void setUp() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn(testEntity.getUsername());

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);
        }

        @Test
        void testUpdatePassword_whenPasswordMatch_shouldUpdatePassword() {
            NewPassword newPassword =
                    new NewPassword()
                            .setCurrentPassword(testEntity.getPassword())
                            .setNewPassword("newPassword");
            when(userRepository.findByUsername(testEntity.getUsername())).thenReturn(Optional.of(testEntity));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(passwordEncoder.encode(newPassword.getNewPassword())).thenReturn("updatedPassword");

            userServiceImpl.updatePassword(newPassword);

            verify(userRepository, times(1)).findByUsername(testEntity.getUsername());
            verify(userRepository, times(1)).save(testEntity);
            verify(passwordEncoder, times(1)).encode(newPassword.getNewPassword());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        }

        @Test
        void testUpdatePassword_whenPassportDoesNotMatch_shouldThrowException() {
            NewPassword newPassword =
                    new NewPassword()
                            .setCurrentPassword("wrongPassword")
                            .setNewPassword("newPassword");
            when(userRepository.findByUsername("username")).thenReturn(Optional.of(testEntity));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            assertThatThrownBy(() -> userServiceImpl.updatePassword(newPassword))
                    .isInstanceOf(PasswordDoesNotMatchException.class);

            verify(userRepository, times(1)).findByUsername("username");
        }

        @Test
        void testUpdatePassword_whenUserEntityNotFound_shouldThrowException() {
            NewPassword newPassword =
                    new NewPassword()
                            .setCurrentPassword("password")
                            .setNewPassword("newPassword");
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.updatePassword(newPassword))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, times(1)).findByUsername("username");
        }

        @Test
        void testGetUser_whenAuthorizedUser_shouldReturnUserData() {
            User expected = mock(User.class);
            when(userRepository.findByUsername("username")).thenReturn(Optional.of(testEntity));
            when(userMapper.map(testEntity)).thenReturn(expected);

            User actual = userServiceImpl.getUser();

            verify(userMapper).map(testEntity);
            verify(userRepository, times(1)).findByUsername("username");

            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testGetUser_whenUserEntityNotFound_shouldThrowException() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.getUser())
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, times(1)).findByUsername("username");
        }

        @Test
        void testUpdateUser_whenUserEntityUpdated_shouldReturnUpdateData() {
            UpdateUser expected = mock(UpdateUser.class);
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.of(testEntity));
            when(userMapper.map(expected, testEntity))
                    .thenReturn(testEntity);

            UpdateUser actual = userServiceImpl.updateUser(expected);

            verify(userMapper).map(expected, testEntity);
            verify(userRepository, times(1)).save(testEntity);

            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testUpdateUser_whenUserEntityNotFound_shouldThrowException() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.updateUser(mock(UpdateUser.class)))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, times(1)).findByUsername(anyString());
        }

        @Test
        void testUpdateOrCreateUserImage_whenImageSuccessSaved_shouldReturnSavedImage() {
            MultipartFile file = mock(MultipartFile.class);
            testEntity.setImage(null);
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.of(testEntity));
            when(userRepository.save(testEntity))
                    .thenReturn(testEntity);

            userServiceImpl.updateOrCreateUserImage(file);

            verify(userRepository, times(1))
                    .findByUsername(testEntity.getUsername());
            verify(userRepository, times(1))
                    .save(testEntity);
            verify(imageServiceImpl, times(1))
                    .saveImage(any(MultipartFile.class));
        }

        @Test
        void testUpdateOrCreateUserImage_whenImageSuccessUpdated_shouldReturnSavedImage() {
            MultipartFile file = mock(MultipartFile.class);
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.of(testEntity));
            when(userRepository.save(testEntity))
                    .thenReturn(testEntity);

            userServiceImpl.updateOrCreateUserImage(file);

            verify(userRepository, times(1))
                    .findByUsername(testEntity.getUsername());
            verify(userRepository, times(1))
                    .save(testEntity);
            verify(imageServiceImpl, times(1))
                    .updateImage(any(MultipartFile.class), anyInt());
        }

        @Test
        void testUpdateOrCreateUserImage_whenUserNotFound_shouldThrowException() {
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.updateOrCreateUserImage(mock(MultipartFile.class)))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void testUpdateOrCreateUserImage_whenUnsuccessfulImageSavingByUpdating_shouldThrowException() {
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.of(testEntity));
            when(imageServiceImpl.updateImage(any(MultipartFile.class), anyInt()))
                    .thenThrow(ErrorImageProcessingException.class);

            assertThatThrownBy(() -> userServiceImpl.updateOrCreateUserImage(mock(MultipartFile.class)))
                    .isInstanceOf(ErrorImageProcessingException.class);
        }

        @Test
        void testUpdateOrCreateUserImage_whenUnsuccessfulImageSavingBySaving_shouldThrowException() {
            testEntity.setImage(null);
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.of(testEntity));
            when(imageServiceImpl.saveImage(any(MultipartFile.class)))
                    .thenThrow(ErrorImageProcessingException.class);

            assertThatThrownBy(() -> userServiceImpl.updateOrCreateUserImage(mock(MultipartFile.class)))
                    .isInstanceOf(ErrorImageProcessingException.class);
        }

        @Test
        void testGetCurrentUser_whenUserFound_shouldReturnCurrentUser() {
            when(userRepository.findByUsername(testEntity.getUsername()))
                    .thenReturn(Optional.of(testEntity));

            UserEntity actual = userServiceImpl.getCurrentUser();

            verify(userRepository, times(1))
                    .findByUsername(testEntity.getUsername());

            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(testEntity);
        }

        @Test
        void testGetCurrentUser_whenUserEntityNotFound_shouldThrowException() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.getCurrentUser())
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, times(1)).findByUsername(anyString());
        }
    }
}