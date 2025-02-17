package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class CommentController {
    private static final String AD_COMMENTS_URL = "/{id}/comments";
    private static final String COMMENT_URL = "/{adId}/comments/{commentId}";

    @Operation(
            tags = {"Комментарии"},
            summary = "Получение комментариев объявления",
            operationId = "getComments",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/Comments")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @GetMapping(AD_COMMENTS_URL)
    public List<Comments> getComments(@PathVariable int id) {
        return Collections.emptyList();
    }

    @Operation(
            tags = {"Комментарии"},
            summary = "Добавление комментария к объявлению",
            operationId = "addComment",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/CreateOrUpdateComment")
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/Comment")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PostMapping(AD_COMMENTS_URL)
    public Comment addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        return new Comment();
    }

    @Operation(
            tags = {"Комментарии"},
            summary = "Удаление комментария",
            operationId = "deleteComment",
            parameters = {
                    @Parameter(
                            name = "adId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    ),
                    @Parameter(
                            name = "commentId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @DeleteMapping(COMMENT_URL)
    public void deleteComment(@PathVariable ("adId") int id, @PathVariable int commentId) {
    }

    @Operation(
            tags = {"Комментарии"},
            summary = "Обновление комментария",
            operationId = "updateComment",
            parameters = {
                    @Parameter(
                            name = "adId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    ),
                    @Parameter(
                            name = "commentId",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/CreateOrUpdateComment")
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PatchMapping(COMMENT_URL)
    public CreateOrUpdateComment updateComment(@PathVariable ("adId") int adId, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        return comment;
    }
}
