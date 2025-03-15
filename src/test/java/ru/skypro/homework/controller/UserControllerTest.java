package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final String TEST_EMAIL = "testEmail@email.com";
    private static final String TEST_FIRST_NAME = "testFirstName";
    private static final String TEST_LAST_NAME = "testLastName";
    private static final String NEW_PASSWORD = "newPassword123";
    private static final String UPDATED_FIRST_NAME = "updatedFirstName";
    private static final String UPDATED_LAST_NAME = "updatedLastName";

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        objectMapper = new ObjectMapper();
        testUser =
                new User()
                        .setFirstName(TEST_FIRST_NAME)
                        .setLastName(TEST_LAST_NAME)
                        .setEmail(TEST_EMAIL);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL, roles = "USER")
    void testSetPassword() throws Exception {
        mockMvc
                .perform(
                        post("/users/set_password")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(
                                                new NewPassword()
                                                        .setNewPassword(NEW_PASSWORD))
                                ))
                .andExpect(status().isOk());

        verify(userService).updatePassword(eq(new NewPassword().setNewPassword(NEW_PASSWORD)));
        verify(authService, times(1)).clearSecurityContext(any(), any());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL, roles = "USER")
    void testGetUser() throws Exception {
        when(userService.getUser()).thenReturn(testUser);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(TEST_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(TEST_LAST_NAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));

        verify(userService, times(1)).getUser();
    }

    @Test
    @WithMockUser(username = TEST_EMAIL, roles = "USER")
    void testUpdateUser() throws Exception {
        UpdateUser updateUser =
                new UpdateUser()
                        .setFirstName(UPDATED_FIRST_NAME)
                        .setLastName(UPDATED_LAST_NAME);

        when(userService.updateUser(any(UpdateUser.class))).thenReturn(updateUser);

        mockMvc.perform(patch("/users/me").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(UPDATED_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(UPDATED_LAST_NAME));

        verify(userService, times(1)).updateUser(any(UpdateUser.class));

    }

    @Test
    @WithMockUser(username = TEST_EMAIL, roles = "USER")
    void testUpdateUserImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test content".getBytes()
        );
        mockMvc
                .perform(
                        multipart(HttpMethod.PATCH, "/users/me/image")
                                .file(file)
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(userService, times(1)).updateOrCreateUserImage(eq(file));
    }
}
