package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.CommentEntity;

@Component
public class CommentMapper {
    public Comment toAd(CommentEntity commentEntity) {
        Comment comment = new Comment();
        comment.setPk(commentEntity.getPk());
        comment.setAuthor(commentEntity.getAuthor());
        comment.setAuthorImage(commentEntity.getAuthorImage());
        comment.setAuthorFirstName(commentEntity.getAuthorFirstName());
        comment.setCreatedAt(commentEntity.getCratedAt());
        comment.setText(commentEntity.getText());
        return comment;
    }

    public CommentEntity toAdEntity(Comment comment) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPk(comment.getPk());
        commentEntity.setAuthor(comment.getAuthor());
        commentEntity.setAuthorImage(comment.getAuthorImage());
        commentEntity.setAuthorFirstName(comment.getAuthorFirstName());
        commentEntity.setCratedAt(comment.getCreatedAt());
        commentEntity.setText(comment.getText());
        return commentEntity;
    }
}
