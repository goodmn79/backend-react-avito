package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    private static final String USERNAME = "email@email.com";
    private static final String TEXT = "Comment text";
    private static final String UPDATED_TEXT = "Updated comment text";

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private ObjectMapper objectMapper;
    private Comment testComment;
    private Comments comments;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        objectMapper = new ObjectMapper();

        testComment =
                new Comment()
                        .setPk(1)
                        .setText(TEXT);

        comments =
                new Comments()
                        .setCount(1)
                        .setResults(List.of(testComment));
    }

    @Test
    @WithMockUser(username = USERNAME, roles = "USER")
    void testGetComments() throws Exception {
        when(commentService.getAdComments(1)).thenReturn(comments);

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].text").value(TEXT));

        verify(commentService, times(1)).getAdComments(1);
    }

    @Test
    @WithMockUser(username = USERNAME, roles = "USER")
    void testAddComment() throws Exception {
        CreateOrUpdateComment request =
                new CreateOrUpdateComment()
                        .setText(TEXT);
        when(commentService.addComment(anyInt(), any(CreateOrUpdateComment.class)))
                .thenReturn(testComment);

        mockMvc
                .perform(
                        post("/ads/1/comments")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(TEXT));

        verify(commentService).addComment(eq(1), eq(request));
    }

    @Test
    @WithMockUser(username = USERNAME, roles = "ADMIN")
    void deleteComment() throws Exception {
        int adId = 1;
        int commentId = 1;
        doNothing().when(commentService).deleteComment(adId, commentId);

        mockMvc.perform(delete("/ads/1/comments/1").with(csrf()))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteComment(eq(adId), eq(commentId));
    }

    @Test
    @WithMockUser(username = USERNAME, roles = "USER")
    void updateComment() throws Exception {
        CreateOrUpdateComment updatedComment =
                new CreateOrUpdateComment()
                        .setText(UPDATED_TEXT);
        when(commentService.updateComment(anyInt(), anyInt(), any(CreateOrUpdateComment.class)))
                .thenReturn(testComment.setText(UPDATED_TEXT));

        mockMvc.perform(patch("/ads/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(UPDATED_TEXT));

        verify(commentService, times(1))
                .updateComment(eq(1), eq(1), eq(updatedComment));
    }
}