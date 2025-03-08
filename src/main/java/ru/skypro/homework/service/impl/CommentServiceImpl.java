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
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

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
        log.warn("Обновление комментария.");

        CommentEntity adComment = this.getCommentByAdId(adId, commentId);

        adComment.setText(comment.getText());

        CommentEntity updatedEntity = commentRepository.save(adComment);

        log.info("Комментарий успешно обновлен.");
        return commentMapper.map(updatedEntity);
    }

    /**
     * Удаление комментария.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     */
    @Override
    public void deleteComment(int adId, int commentId) {
        log.warn("Удаление комментария.");

        commentRepository.delete(this.getCommentByAdId(adId, commentId));

        log.info("Комментарий успешно удален.");
    }

    /**
     * Проверка текущего пользователя на авторство комментария.
     *
     * @return {@code true}, если текущий пользователь является автором комментария, иначе {@code false}
     */
    @Override
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

    /**
     * Получение всех комментариев объявления.
     *
     * @param adId идентификатор объявления
     * @return список комментариев {@link CommentEntity}
     */
    private List<CommentEntity> getAdCommentEntities(int adId) {
        return commentRepository.findAllByAdPk(adId);
    }

    /**
     * Получение комментария объявления.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @return объект {@link CommentEntity} содержащий комментарий
     * @throws CommentNotFoundException Если комментарий не найден
     */
    private CommentEntity getCommentByAdId(int adId, int commentId) {
        return this.getAdCommentEntities(adId)
                .stream()
                .filter(c -> c.getPk() == commentId)
                .findFirst().orElseThrow(CommentNotFoundException::new);
    }
}
