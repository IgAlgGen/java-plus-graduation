package ewm.comment.controller;

import ewm.comment.dto.CommentDto;
import ewm.comment.service.CommentService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentPublicController {
    private final CommentService commentService;

    /**
     * Возвращает одобренные комментарии события.
     *
     * @param eventId идентификатор события
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список комментариев события
     */
    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getEventComments(eventId, from, size);
    }

    /**
     * Возвращает одобренные комментарии по всем событиям.
     *
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список комментариев
     */
    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getComments(from, size);
    }
}
