package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.Validatable;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.service.UserService;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserService userService;
    private final Validatable validator;

    public Comment map(CommentEntity commentEntity) {
        return new Comment()
                .setPk(commentEntity.getPk())
                .setAuthor(commentEntity.getAuthor().getId())
                .setAuthorImage(commentEntity.getAuthor().getImage().getPath())
                .setAuthorFirstName(commentEntity.getAuthor().getFirstName())
                .setCreatedAt(commentEntity.getCratedAt())
                .setText(commentEntity.getText());
    }

    public CommentEntity map(CreateOrUpdateComment comment) {
        return new CommentEntity()
                .setAuthor(userService.getCurrentUser())
                .setCratedAt(System.currentTimeMillis())
                .setText(validator.validate(comment.getText(), 8, 64));
    }
}
