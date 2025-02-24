package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class CommentController {
    private CommentService commentService;

    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable int id) {
        return commentService.getComments(id);
    }

    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        return commentService.addComment(id, comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        commentService.deleteComment(adId, commentId);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public Comment updateComment(@PathVariable int adId, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        return commentService.updateComment(adId, commentId, comment);
    }
}
