package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

public interface CommentService {
    Comments getAdComments(int id);

    Comment addComment(int id, CreateOrUpdateComment comment);

    Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment);

    void deleteComment(int adId, int commentId);

    boolean isCommentAuthor(int adId, int commentId, String currentUsername);
}
