package ewm.request.service;

import ru.practicum.ewm.internal.dto.EventConfirmedRequestsInternalDto;
import ru.practicum.ewm.internal.dto.ParticipationRequestInternalDto;

import java.util.List;
import java.util.Map;

/**
 * Внутренний сервис чтения заявок на участие для других микросервисов.
 */
public interface ParticipationRequestInternalService {
    /**
     * Возвращает количество подтвержденных заявок по событиям.
     *
     * @param eventIds идентификаторы событий
     * @return счетчики подтвержденных заявок только для событий, где такие заявки есть
     */
    List<EventConfirmedRequestsInternalDto> getConfirmedCounts(List<Long> eventIds);

    /**
     * Возвращает заявки конкретного события.
     *
     * @param eventId идентификатор события
     * @return список заявок события
     */
    List<ParticipationRequestInternalDto> getEventRequests(Long eventId);

    /**
     * Проверяет, есть ли у пользователя заявки на указанные события.
     *
     * @param userId идентификатор пользователя
     * @param eventIds идентификаторы событий
     * @return карта {@code eventId -> заявка существует}
     */
    Map<Long, Boolean> existsUserRequestsForEvents(Long userId, List<Long> eventIds);

    Map<Long, Boolean> existsUserConfirmedRequestsForEvents(Long userId, List<Long> eventIds);
}
