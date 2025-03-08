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


/**
 * Маппер для преобразования комментариев к объявлениям между DTO и сущностями.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserServiceImpl userServiceImpl;

    /**
     * Преобразует {@link CommentEntity} в {@link Comment}.
     *
     * @param commentEntity Сущность, которую нужно преобразовать
     * @return {@link Comment} DTO содержащий комментарий к объявлению и дополняющую его информацию
     */
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

    /**
     * Преобразует {@link CreateOrUpdateComment} в {@link CommentEntity}.
     *
     * @param comment DTO, который нужно преобразовать
     * @return {@link CommentEntity}, сущность содержащая комментарий к объявлению
     */
    public CommentEntity map(CreateOrUpdateComment comment) {
        return new CommentEntity()
                .setAuthor(userServiceImpl.getCurrentUser())
                .setCratedAt(System.currentTimeMillis())
                .setText(DataValidator.validatedData(comment.getText(), 8, 64));
    }

    /**
     * Преобразует список {@link CommentEntity} в {@link Comments}.
     *
     * @param comments Список комментариев, который нужно преобразовать
     * @return {@link Comments}, объект DTO содержащий информацию об общем количестве комментариев и их список
     */
    public Comments map(List<CommentEntity> comments) {
        return new Comments()
                .setCount(comments.size())
                .setResults(comments
                        .stream()
                        .map(this::map)
                        .collect(Collectors.toUnmodifiableList()));
    }
}
