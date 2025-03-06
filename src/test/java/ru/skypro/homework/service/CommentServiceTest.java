package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.component.mapper.CommentMapper;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AdService adService;

    @InjectMocks
    private CommentService commentService;

    private final int AD_ID = 1;
    private final int COMMENT_ID = 1;
    private final String TEXT = "Test comment";
    private final String USERNAME = "user@mail.com";

    @Test
    void getAdComments_ShouldReturnComments() {

        List<CommentEntity> entities = List.of(new CommentEntity());
        when(commentRepository.findAllByAdPk(AD_ID)).thenReturn(entities);
        when(commentMapper.map(entities)).thenReturn(new Comments());

        Comments result = commentService.getAdComments(AD_ID);

        assertNotNull(result);
        verify(commentRepository).findAllByAdPk(AD_ID);
    }

    @Test
    void addComment_ShouldCreateAndReturnComment() {

        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText(TEXT);

        AdEntity ad = new AdEntity();
        CommentEntity mappedEntity = new CommentEntity().setText(TEXT).setAd(ad);
        CommentEntity savedEntity = new CommentEntity(); // Предположим, что после сохранения есть ID
        Comment expectedComment = new Comment();

        when(adService.getAdEntity(AD_ID)).thenReturn(ad);
        when(commentMapper.map(dto)).thenReturn(mappedEntity);
        when(commentRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(commentMapper.map(savedEntity)).thenReturn(expectedComment);

        Comment result = commentService.addComment(AD_ID, dto);

        assertEquals(expectedComment, result);
        verify(commentRepository).save(mappedEntity);
    }

    @Test
    void updateComment_ShouldUpdateCommentText() {

        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("New text");

        CommentEntity existingEntity = new CommentEntity();
        existingEntity.setPk(COMMENT_ID);
        existingEntity.setText("Old text");

        when(commentRepository.findAllByAdPk(AD_ID)).thenReturn(List.of(existingEntity));
        when(commentRepository.save(existingEntity)).thenReturn(existingEntity);
        when(commentMapper.map(existingEntity)).thenReturn(new Comment());

        Comment result = commentService.updateComment(AD_ID, COMMENT_ID, dto);

        assertEquals("New text", existingEntity.getText());
        verify(commentRepository).save(existingEntity);
    }

    @Test
    void deleteComment_ShouldInvokeRepositoryDelete() {

        CommentEntity entity = new CommentEntity();
        entity.setPk(COMMENT_ID);

        when(commentRepository.findAllByAdPk(AD_ID)).thenReturn(List.of(entity));

        commentService.deleteComment(AD_ID, COMMENT_ID);

        verify(commentRepository).delete(entity);
        verify(commentRepository).findAllByAdPk(AD_ID); // Дополнительная проверка вызова
    }

    @Test
    void isCommentAuthor_WhenAuthorMatches_ShouldReturnTrue() {

        UserEntity author = new UserEntity();
        author.setUsername(USERNAME);

        CommentEntity comment = new CommentEntity();
        comment.setPk(COMMENT_ID); // Устанавливаем ID комментария
        comment.setAuthor(author);

        AdEntity ad = new AdEntity();
        ad.setPk(AD_ID);
        comment.setAd(ad);

        when(commentRepository.findAllByAdPk(AD_ID)).thenReturn(List.of(comment));

        boolean result = commentService.isCommentAuthor(AD_ID, COMMENT_ID, USERNAME);

        assertTrue(result);
        verify(commentRepository).findAllByAdPk(AD_ID); // Проверяем вызов
    }
    @Test
    void getCommentByAdId_WhenNotFound_ShouldThrowException() {

        when(commentRepository.findAllByAdPk(AD_ID)).thenReturn(List.of());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.getCommentByAdId(AD_ID, COMMENT_ID));
    }
}