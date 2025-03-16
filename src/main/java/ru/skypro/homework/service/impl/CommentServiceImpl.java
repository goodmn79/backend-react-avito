package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.component.mapper.CommentMapper;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.exception.AccessNotEnoughException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.util.List;

/**
 * Реализация сервиса для работы с изображениями.
 * <br> Этот класс реализует интерфейс {@link CommentService}
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdService adService;
    private final UserService userService;

    /**
     * Получение комментариев объявления.
     *
     * @param id идентификатор объявления
     * @return объект {@link Comments} с информацией об общем количестве комментариев и их список
     */
    @Override
    public Comments getAdComments(int id) {
        log.info("Получение всех комментариев.");

        return commentMapper.map(this.getAdCommentEntities(id));
    }

    /**
     * Добавление комментария к объявлению.
     *
     * @param id      идентификатор объявления
     * @param comment текст комментария
     * @return объект {@link Comment} с информацией о комментарии
     */
    @Override
    public Comment addComment(int id, CreateOrUpdateComment comment) {
        log.info("Добавление комментария.");

        AdEntity ad = adService.getAdEntity(id);

        CommentEntity entity =
                commentMapper.map(comment).setAd(ad);

        log.info("Комментарий успешно создан.");
        return commentMapper
                .map(commentRepository.save(entity));
    }

    /**
     * Обновление комментария.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @param comment   текст нового комментария
     * @return объект {@link Comment} с информацией о новом комментарии
     */
    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment) {
        log.warn("Comment update...");

        if (this.isCommentAuthor(adId, commentId)) {
            CommentEntity adComment =
                    this.getCommentByAdId(adId, commentId)
                            .setText(comment.getText());

            CommentEntity updatedEntity = commentRepository.save(adComment);

            log.info("The comment is successfully updated.");
            return commentMapper.map(updatedEntity);
        } else {
            log.error("Access is not enough to update an comment");
            throw new AccessNotEnoughException();
        }
    }

    /**
     * Удаление комментария.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     */
    @Override
    public void deleteComment(int adId, int commentId) {
        log.warn("Comment removal...");

        if (this.isCommentAuthor(adId, commentId) ||  userService.isAdmin()) {
            commentRepository.delete(this.getCommentByAdId(adId, commentId));
            log.info("The comment is successfully deleted.");
        } else {
            log.error("Access is not enough to remove an comment.");
            throw new AccessNotEnoughException();
        }
    }

    /**
     * Проверка текущего пользователя на авторство комментария.
     *
     * @return {@code true}, если текущий пользователь является автором комментария, иначе {@code false}
     */
    @Override
    public boolean isCommentAuthor(int adId, int commentId) {
        String commentAuthorName =
                this.getCommentByAdId(adId, commentId).getAuthor().getUsername();

        return commentAuthorName.equals(userService.getCurrentUser().getUsername());
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
}
