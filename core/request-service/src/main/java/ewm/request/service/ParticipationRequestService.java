package ewm.request.service;

import ewm.request.dto.EventRequestStatusUpdateRequest;
import ewm.request.dto.EventRequestStatusUpdateResult;
import ewm.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Сервис управления заявками на участие в событиях.
 */
public interface ParticipationRequestService {

    /**
     * Создает заявку пользователя на участие в событии.
     *
     * @param userId идентификатор заявителя
     * @param eventId идентификатор события
     * @return созданная заявка
     * @throws ewm.common.exception.ConflictException если заявитель является инициатором, событие не опубликовано,
     *                                                заявка уже существует или достигнут лимит участников
     */
    ParticipationRequestDto create(Long userId, Long eventId);

    /**
     * Отменяет заявку ее владельцем.
     *
     * @param userId идентификатор заявителя
     * @param requestId идентификатор заявки
     * @return заявка в статусе отмены
     * @throws ewm.common.exception.NotFoundException если заявка пользователя не найдена
     */
    ParticipationRequestDto cancel(Long userId, Long requestId);

    /**
     * Возвращает все заявки пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список заявок пользователя
     */
    List<ParticipationRequestDto> getUserRequests(Long userId);

    /**
     * Возвращает заявки на событие его инициатору.
     *
     * @param userId идентификатор инициатора события
     * @param eventId идентификатор события
     * @return заявки на событие
     * @throws ewm.common.exception.ConflictException если пользователь не является инициатором
     */
    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    /**
     * Подтверждает или отклоняет набор заявок на событие.
     *
     * <p>Изменять можно только заявки в статусе ожидания. При подтверждении заявки сверх лимита
     * автоматически переводятся в отклоненные.</p>
     *
     * @param userId идентификатор инициатора события
     * @param eventId идентификатор события
     * @param requestIds идентификаторы заявок
     * @param status целевой статус: подтверждение или отклонение
     * @return раздельные списки подтвержденных и отклоненных заявок
     * @throws ewm.common.exception.ConflictException если нарушены права или лимит участников
     * @throws ewm.common.exception.NotFoundException если часть заявок не найдена для события
     */
    EventRequestStatusUpdateResult updateEventRequests(
            Long userId,
            Long eventId,
            List<Long> requestIds,
            EventRequestStatusUpdateRequest.RequestUpdateStatus status
    );
}
