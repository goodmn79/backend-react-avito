package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setEmail("testEmail@email.com");
    }

    @Test
    @WithMockUser(username = "testEmail@email.com", roles = "USER")
    void testSetPassword() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPassword("newPassword123");


        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"newPassword\": \"newPassword123\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updatePassword(any(NewPassword.class));
        verify(authService, times(1)).clearSecurityContext(any(), any());
    }

    @Test
    @WithMockUser(username = "testEmail@email.com", roles = "USER")
    void testGetUser() throws Exception {
        when(userService.getUser()).thenReturn(testUser);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("testFirstName"))
                .andExpect(jsonPath("$.lastName").value("testLastName"))
                .andExpect(jsonPath("$.email").value("testEmail@email.com"));

        verify(userService, times(1)).getUser();
    }

    @Test
    @WithMockUser(username = "testEmail@email.com", roles = "USER")
    void testUpdateUser() throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("updatedFirstName");
        updateUser.setLastName("updatedLastName");

        when(userService.updateUser(any(UpdateUser.class))).thenReturn(updateUser);

        mockMvc.perform(patch("/users/me").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"updatedFirstName\", \"lastName\": \"updatedLastName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("updatedFirstName"))
                .andExpect(jsonPath("$.lastName").value("updatedLastName"));

        verify(userService, times(1)).updateUser(any(UpdateUser.class));

    }

    @Test
    @WithMockUser(username = "testEmail@email.com", roles = "USER")
    void testUpdateUserImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test content".getBytes()
        );
        mockMvc.perform(multipart(HttpMethod.PATCH,"/users/me/image")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserImage(any(MultipartFile.class));
    }
}