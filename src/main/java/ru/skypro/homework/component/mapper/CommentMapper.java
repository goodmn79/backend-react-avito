package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserServiceImpl userServiceImpl;

    public Comment map(CommentEntity commentEntity) {
        String imagePath = commentEntity.getAuthor().getImage() == null ? null : commentEntity.getAuthor().getImage().getPath();
        return new Comment()
                .setPk(commentEntity.getPk())
                .setAuthor(commentEntity.getAuthor().getId())
                .setAuthorImage(imagePath)
                .setAuthorFirstName(commentEntity.getAuthor().getFirstName())
                .setCreatedAt(commentEntity.getCratedAt())
                .setText(commentEntity.getText());
    }

    public CommentEntity map(CreateOrUpdateComment comment) {
        return new CommentEntity()
                .setAuthor(userServiceImpl.getCurrentUser())
                .setCratedAt(System.currentTimeMillis())
                .setText(DataValidator.validatedData(comment.getText(), 8, 64));
    }

    public Comments map(List<CommentEntity> comments) {
        return new Comments()
                .setCount(comments.size())
                .setResults(comments
                        .stream()
                        .map(this::map)
                        .collect(Collectors.toUnmodifiableList()));
    }
}
