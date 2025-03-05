package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

@ActiveProfiles("test")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Comment testComment;

    private Comments comments;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setPk(1);
        testComment.setText("Test comment");

        comments = new Comments().setResults(List.of(testComment));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetComments() throws Exception {
        when(commentService.getAdComments(1)).thenReturn(comments);

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].text").value("Test comment"));

        verify(commentService, times(1)).getAdComments(1);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddComment() throws Exception {
        CreateOrUpdateComment request = new CreateOrUpdateComment();
        request.setText("New comment");

        Comment response = new Comment();
        response.setText("New comment");
        response.setPk(1);
        response.setAuthor(0);
        response.setCreatedAt(0L);

        when(commentService.addComment(anyInt(),any(CreateOrUpdateComment.class))).thenReturn(response);



        mockMvc.perform(post("/ads/1/comments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"New comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New comment"));

        verify(commentService).addComment(eq(1),argThat(c -> c.getText().equals("New comment")));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(1,1);

        mockMvc.perform(delete("/ads/1/comments/1").with(csrf()))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteComment(1,1);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateComment() throws Exception {
        CreateOrUpdateComment updatedComment = new CreateOrUpdateComment();
        updatedComment.setText("Updated comment");
        testComment.setText("Updated comment");
        when(commentService.updateComment(anyInt(),anyInt(),any(CreateOrUpdateComment.class))).thenReturn(testComment);

        mockMvc.perform(patch("/ads/1/comments/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"Updated comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated comment"));

        verify(commentService, times(1)).updateComment(anyInt(),anyInt(),any(CreateOrUpdateComment.class));
    }
}