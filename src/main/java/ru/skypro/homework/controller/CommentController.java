package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {
    private static final String AD_COMMENTS_URL = "/{id}/comments";
    private static final String COMMENT_URL = "/{adId}/comments/{commentId}";

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping(AD_COMMENTS_URL)
    public List<Comments> getComments(@PathVariable int id) {
        return Collections.emptyList();
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping(AD_COMMENTS_URL)
    public Comment addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        return new Comment();
    }

    @Operation(summary = "Удаление комментария",
            operationId = "deleteComment")
    @DeleteMapping(COMMENT_URL)
    public void deleteComment(@PathVariable("adId") int id, @PathVariable int commentId) {
    }

    @Operation(summary = "Обновление комментария")
    @PatchMapping(COMMENT_URL)
    public CreateOrUpdateComment updateComment(@PathVariable("adId") int adId, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        return comment;
    }
}
