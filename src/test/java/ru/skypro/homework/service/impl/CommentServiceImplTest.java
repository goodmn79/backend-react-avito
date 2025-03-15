package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.component.mapper.CommentMapper;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AdService adService;

    @InjectMocks
    private CommentServiceImpl service;

    private CommentEntity comment;
    private int adId;
    private int commentId;
    private String text;
    private String username;

    @BeforeEach
    void setUp() {
        adId = 1;

        commentId = 1;

        text = "Test comment";

        username = "user@gmail.com";

        comment =
                new CommentEntity()
                        .setPk(commentId)
                        .setAuthor(
                                new UserEntity()
                                        .setUsername(username)
                        );
    }

    @Test
    void getAdComments_whenCommentsExist_shouldReturnComments() {
        Comment mockComment = mock(Comment.class);
        Comments comments =
                new Comments()
                        .setCount(1)
                        .setResults(List.of(mockComment));
        List<CommentEntity> commentList = List.of(comment);
        when(commentRepository.findAllByAdPk(adId)).thenReturn(commentList);
        when(commentMapper.map(commentList)).thenReturn(comments);

        Comments actual = service.getAdComments(adId);

        verify(commentRepository).findAllByAdPk(adId);
        verify(commentMapper).map(commentList);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(comments);
        assertThat(actual.getCount()).isEqualTo(comments.getCount());
        assertThat(actual.getResults()).contains(mockComment);
    }

    @Test
    void addComment_whenCreateNewComment_shouldReturnThisComment() {
        CreateOrUpdateComment commentData =
                new CreateOrUpdateComment()
                        .setText(text);
        Comment expected =
                new Comment()
                        .setText(text);
        when(commentMapper.map(any(CreateOrUpdateComment.class))).thenReturn(comment);
        when(commentRepository.save(any())).thenReturn(comment);
        when(commentMapper.map(comment)).thenReturn(expected);

        Comment actual = service.addComment(adId, commentData);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getText()).isEqualTo(expected.getText());
        verify(adService).getAdEntity(anyInt());
        verify(commentRepository).save(any());
        verify(commentMapper).map(any(CreateOrUpdateComment.class));
        verify(commentMapper).map(comment);
    }

    @Test
    void updateComment_whenUpdateComment_shouldReturnUpdateDComment() {
        String newText = "new text";
        CreateOrUpdateComment commentData =
                new CreateOrUpdateComment()
                        .setText(newText);
        Comment expected =
                new Comment()
                        .setText(newText);
        when(commentRepository.findAllByAdPk(adId)).thenReturn(List.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.map(comment)).thenReturn(expected);

        Comment actual = service.updateComment(adId, commentId, commentData);

        assertThat(actual).isNotNull();
        assertThat(expected).isEqualTo(actual);
        assertThat(expected.getText()).isEqualTo(actual.getText());
        verify(commentRepository).findAllByAdPk(adId);
        verify(commentRepository).save(comment);
        verify(commentMapper).map(comment);
    }

    @Test
    void deleteComment_whenCommentExist_shouldDeleteThisComment() {
        when(commentRepository.findAllByAdPk(anyInt())).thenReturn(List.of(comment));

        service.deleteComment(adId, commentId);

        verify(commentRepository).delete(comment);
        verify(commentRepository).findAllByAdPk(adId);
    }

    @Test
    void deleteComment_whenCommentNotFound_shouldThrowException() {
        when(commentRepository.findAllByAdPk(anyInt())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.deleteComment(adId, anyInt()))
                .isInstanceOf(CommentNotFoundException.class);

        verify(commentRepository).findAllByAdPk(adId);
    }

    @Test
    void isCommentAuthor_whenAuthorMatches_shouldReturnTrue() {
        when(commentRepository.findAllByAdPk(adId)).thenReturn(List.of(comment));

        boolean actual = service.isCommentAuthor(adId, commentId, username);

        assertThat(actual).isTrue();
        verify(commentRepository).findAllByAdPk(adId);
    }

    @Test
    void isCommentAuthor_whenAuthorNotMatches_shouldReturnFalse() {
        when(commentRepository.findAllByAdPk(adId)).thenReturn(List.of(comment));

        boolean actual = service.isCommentAuthor(adId, commentId, "another@gmail.com");

        assertThat(actual).isFalse();
        verify(commentRepository).findAllByAdPk(adId);
    }

    @Test
    void isCommentAuthor_whenCommentNotFound_shouldThrowException() {
        when(commentRepository.findAllByAdPk(adId)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.isCommentAuthor(adId, commentId, username))
                .isInstanceOf(CommentNotFoundException.class);

        verify(commentRepository).findAllByAdPk(adId);
    }
}