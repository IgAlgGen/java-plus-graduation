package ewm.comment.controller;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.NewCommentDto;
import ewm.comment.dto.UpdateCommentRequest;
import ewm.comment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер пользовательских операций с комментариями.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users/{userId}")
public class CommentPrivateController {
    private final CommentService commentService;

    /**
     * Создает комментарий к опубликованному событию.
     *
     * @param userId идентификатор автора
     * @param eventId идентификатор события
     * @param newCommentDto текст комментария
     * @return созданный комментарий
     */
    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId,
                             @PathVariable @Positive Long eventId,
                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.create(userId, eventId, newCommentDto);
    }

    /**
     * Обновляет комментарий автора.
     *
     * @param userId идентификатор автора
     * @param commentId идентификатор комментария
     * @param updateCommentRequest новый текст
     * @return обновленный комментарий
     */
    @PatchMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable @Positive Long commentId,
                             @RequestBody @Valid UpdateCommentRequest updateCommentRequest) {
        return commentService.update(userId, commentId, updateCommentRequest);
    }

    /**
     * Удаляет комментарий автора.
     *
     * @param userId идентификатор автора
     * @param commentId идентификатор комментария
     */
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable @Positive Long commentId) {
        commentService.delete(userId, commentId);
    }
}
