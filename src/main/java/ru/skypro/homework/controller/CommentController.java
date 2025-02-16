package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class CommentController {
    private final String AD_COMMENTS_URL = "/{id}/comments";
    private final String COMMENT_URL = AD_COMMENTS_URL + "/{commentId}";

    @GetMapping(AD_COMMENTS_URL)
    public List<Comments> getCommentsByAdId(@PathVariable int id) {
        return Collections.emptyList();
    }

    @PostMapping(AD_COMMENTS_URL)
    public Comment addComments(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        return new Comment();
    }

    @DeleteMapping(COMMENT_URL)
    public void deleteCommentsById(@PathVariable int id, @PathVariable int commentId) {
    }

    @PatchMapping(COMMENT_URL)
    public CreateOrUpdateComment updateComments(@PathVariable int id, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        return comment;
    }
}
