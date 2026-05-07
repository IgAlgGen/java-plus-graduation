package ewm.comment.controller;

import ewm.comment.dto.CommentDto;
import ewm.comment.service.CommentService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Административный REST-контроллер модерации комментариев.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class CommentAdminController {
    private final CommentService commentService;

    /**
     * Одобряет комментарий.
     *
     * @param commentId идентификатор комментария
     * @return одобренный комментарий
     */
    @PatchMapping("/{commentId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto approve(@PathVariable @Positive Long commentId) {
        return commentService.approve(commentId);
    }

    /**
     * Отклоняет комментарий.
     *
     * @param commentId идентификатор комментария
     * @return отклоненный комментарий
     */
    @PatchMapping("/{commentId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto reject(@PathVariable @Positive Long commentId) {
        return commentService.reject(commentId);
    }
}
