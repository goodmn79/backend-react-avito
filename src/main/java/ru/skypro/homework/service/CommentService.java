package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

/**
 * Сервис для работы с комментариями.
 * <br> Этот интерфейс предоставляет методы для операций с комментариями,
 * таких как их добавление, удаление, обновление, получение и проверка авторства.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public interface CommentService {
    Comments getAdComments(int id);

    Comment addComment(int id, CreateOrUpdateComment comment);

    Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment);

    void deleteComment(int adId, int commentId);

    boolean isCommentAuthor(int adId, int commentId, String currentUsername);
}
