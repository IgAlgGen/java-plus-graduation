package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.NewCommentDto;
import ewm.comment.dto.UpdateCommentRequest;

import java.util.List;

/**
 * Сервис пользовательских, публичных и административных операций с комментариями.
 */
public interface CommentService {
    /**
     * Создает комментарий к опубликованному событию.
     *
     * @param userId идентификатор автора
     * @param eventId идентификатор события
     * @param newCommentDto текст комментария
     * @return созданный комментарий в статусе модерации
     */
    CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto);

    /**
     * Обновляет комментарий автора и возвращает его на модерацию.
     *
     * @param userId идентификатор автора
     * @param commentId идентификатор комментария
     * @param updateCommentRequest новый текст
     * @return обновленный комментарий
     * @throws ewm.common.exception.ConflictException если комментарий уже отклонен
     */
    CommentDto update(Long userId, Long commentId, UpdateCommentRequest updateCommentRequest);

    /**
     * Возвращает одобренные комментарии события.
     *
     * @param eventId идентификатор события
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список одобренных комментариев
     */
    List<CommentDto> getEventComments(Long eventId, int from, int size);

    /**
     * Возвращает одобренные комментарии по всем событиям.
     *
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список одобренных комментариев
     */
    List<CommentDto> getComments(int from, int size);

    /**
     * Удаляет комментарий его автором.
     *
     * @param userId идентификатор автора
     * @param commentId идентификатор комментария
     */
    void delete(Long userId, Long commentId);

    /**
     * Одобряет комментарий администратором.
     *
     * @param commentId идентификатор комментария
     * @return комментарий в статусе одобрения
     */
    CommentDto approve(Long commentId);

    /**
     * Отклоняет комментарий администратором.
     *
     * @param commentId идентификатор комментария
     * @return комментарий в статусе отклонения
     */
    CommentDto reject(Long commentId);
}
