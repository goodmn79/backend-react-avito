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
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.NoAccessRightException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdRepository adRepository;
    private final UserService userService;


    private final Logger log = LoggerFactory.getLogger(CommentService.class);


    public Comments getComments(int id) {
        List<CommentEntity> comments = commentRepository.findAllByAdPk(id);
        return commentMapper.map(comments);
    }


    public Comment addComment(int id, CreateOrUpdateComment comment) {
        CommentEntity entity =
                commentMapper.map(comment)
                        .setAd(adRepository.findById(id).get());
        return commentMapper
                .map(commentRepository
                        .save(entity));
    }

    @PreAuthorize("@commentService.isUser(#adId)")
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment) {
        CommentEntity entity = preAuthorize(commentId);
        return commentMapper
                .map(commentRepository
                        .save(entity.setText(comment.getText())));
    }

    @PreAuthorize("@commentService.isUser(#adId)")
    public void deleteComment(int adId, int commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentEntity getCommentEntity(int id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    private CommentEntity preAuthorize(int commentId) {

        CommentEntity entity = this.getCommentEntity(commentId);
        UserEntity author = entity.getAuthor();
        if (author.equals(userService.getCurrentUser())) {
            return entity;
        }
        throw new NoAccessRightException();
    }

    public boolean isUser(int adId) {
        AdEntity ad = adRepository.findById(adId).get();
        UserEntity author = ad.getAuthor();
        UserEntity user = userService.getCurrentUser();
        return author.equals(user) || user.getRole().equals(Role.ADMIN);
    }
}
