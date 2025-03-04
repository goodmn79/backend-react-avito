package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.component.mapper.CommentMapper;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
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

    private final Logger log = LoggerFactory.getLogger(CommentService.class);


    public Comments getAdComments(int id) {
        log.info("Получение всех комментариев.");

        return commentMapper.map(this.getAdCommentEntities(id));
    }

    public Comment addComment(int id, CreateOrUpdateComment comment) {
        log.info("Добавление комментария.");

        AdEntity ad = adService.getAdEntity(id);

        CommentEntity entity =
                commentMapper.map(comment).setAd(ad);

        log.info("Комментарий успешно создан.");
        return commentMapper
                .map(commentRepository.save(entity));
    }

    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment) {
        log.warn("Обновление комментария.");

        CommentEntity adComment = this.getCommentByAdId(adId, commentId);

        adComment.setText(comment.getText());

        CommentEntity updatedEntity = commentRepository.save(adComment);

        log.info("Комментарий успешно обновлен.");
        return commentMapper.map(updatedEntity);
    }

    public void deleteComment(int adId, int commentId) {
        log.warn("Удаление комментария.");

        commentRepository.delete(this.getCommentByAdId(adId, commentId));

        log.info("Комментарий успешно удален.");
    }

    private List<CommentEntity> getAdCommentEntities(int adId) {
        return commentRepository.findAllByAdPk(adId);
    }

    private CommentEntity getCommentByAdId(int adId, int commentId) {
        return this.getAdCommentEntities(adId)
                .stream()
                .filter(c -> c.getPk() == commentId)
                .findFirst().orElseThrow(CommentNotFoundException::new);
    }

    public boolean isCommentAuthor(int adId, int commentId, String currentUsername) {
        String commentAuthorName = this.getCommentByAdId(adId, commentId).getAuthor().getUsername();

        log.warn("Проверка соответствия имени автора комментария: '{}' и имени текущего пользователя: '{}'.", commentAuthorName, currentUsername);

        boolean isAuthor = commentAuthorName.equals(currentUsername);

        if (isAuthor) {
            log.info("Доступ к изменению/удалению комментария разрешён.");
        } else {
            log.error("Доступ к изменению/удалению комментария запрещён!");
        }
        return isAuthor;
    }
}
