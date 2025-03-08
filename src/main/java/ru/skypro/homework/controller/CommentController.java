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
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable int id) {
        log.info("Вызван метод 'getComments'");
        return commentService.getAdComments(id);
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        log.info("Вызван метод 'addComment'");
        return commentService.addComment(id, comment);
    }

    @Operation(summary = "Удаление комментария",
            operationId = "deleteComment")
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @commentService.isCommentAuthor(#adId, #commentId, authentication.principal.username)")
    public void deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        log.info("Вызван метод 'deleteComment'");
        commentService.deleteComment(adId, commentId);
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@commentService.isCommentAuthor(#adId, #commentId, authentication.principal.username)")
    public Comment updateComment(@PathVariable int adId, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        log.info("Вызван метод 'updateComment'");
        return commentService.updateComment(adId, commentId, comment);
    }
}
