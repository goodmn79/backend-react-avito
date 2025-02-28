package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.component.mapper.CommentMapper;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdService adService;
    private final UserService userService;


    private final Logger log = LoggerFactory.getLogger(CommentService.class);


    public Comments getComments(int id) {
        log.info("Получение всех комментариев.");

        List<CommentEntity> comments = commentRepository.findAllByAdPk(id);
        log.info("Комментарии успешно получены.");

        return commentMapper.map(comments);
    }


    public Comment addComment(int id, CreateOrUpdateComment comment) {
        log.info("Создание комментария.");

        CommentEntity entity =
                commentMapper.map(comment).setAd(adService.getAdEntity(id));

        log.info("Комментарий успешно создан.");
        return commentMapper
                .map(commentRepository
                        .save(entity));
    }

    @PreAuthorize("@commentService.isCommentAuthor(#adId)")
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment) {
        log.info("Запрос на обновление комментария.");

        CommentEntity entity = getCommentEntity(commentId);
        entity.setText(comment.getText());

        CommentEntity updatedEntity = commentRepository.save(entity);

        log.info("Комментарий успешно обновлен.");
        return commentMapper.map(updatedEntity);
    }

    @PreAuthorize("hasAuthority('ADMIN') or @commentService.isCommentAuthor(#adId) or @adService.isAdAuthor(adId)")
    public void deleteComment(int adId, int commentId) {
        log.warn("Удаление комментария.");

        commentRepository.deleteById(commentId);

        log.info("Комментарий успешно удален.");
    }

    private CommentEntity getCommentEntity(int id) {
        log.error("Данный комментарий не найден!");
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    public boolean isCommentAuthor(int commentId) {
        int commentAuthorId = this.getCommentEntity(commentId).getAuthor().getId();
        int currentUserId = userService.getCurrentUser().getId();
        return commentAuthorId == currentUserId;
    }
}
