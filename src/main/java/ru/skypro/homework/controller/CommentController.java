package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.impl.CommentServiceImpl;

/**
 * Контроллер для управления комментариями.
 * <p>
 * Обрабатывает HTTP-запросы для создания, получения, обновления и удаления комментариев.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
@RequiredArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentServiceImpl;

    /**
     * Получение комментариев объявления.
     * <br> Endpoint: GET /ads/{id}/comments
     *
     * @param id идентификатор объявления
     * @return объект {@link Comments} с информацией об общем количестве комментариев и их список
     */
    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable int id) {
        log.info("Вызван метод 'getComments'");
        return commentServiceImpl.getAdComments(id);
    }

    /**
     * Добавление комментария к объявлению.
     * <br> Endpoint: POST /ads/{id}/comments
     *
     * @param id      идентификатор объявления
     * @param comment текст комментария
     * @return объект {@link Comment} с информацией о комментарии
     */
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        log.info("Вызван метод 'addComment'");
        return commentServiceImpl.addComment(id, comment);
    }

    /**
     * Удаление комментария.
     * <br> Доступ к методу имеет пользователь с ролью ADMIN, либо автор комментария.
     * <br> Endpoint: DELETE /ads/{adId}/comments/{commentId}
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     */
    @Operation(summary = "Удаление комментария",
            operationId = "deleteComment")
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @commentServiceImpl.isCommentAuthor(#adId, #commentId, authentication.principal.username)")
    public void deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        log.info("Вызван метод 'deleteComment'");
        commentServiceImpl.deleteComment(adId, commentId);
    }

    /**
     * Обновление комментария.
     * <br> Доступ к методу имеет только автор комментария.
     * <br> Endpoint: PATCH /ads/{adId}/comments/{commentId}
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @param comment   текст нового комментария
     * @return объект {@link Comment} с информацией о новом комментарии
     */
    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@commentServiceImpl.isCommentAuthor(#adId, #commentId, authentication.principal.username)")
    public Comment updateComment(@PathVariable int adId, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        log.info("Вызван метод 'updateComment'");
        return commentServiceImpl.updateComment(adId, commentId, comment);
    }
}
