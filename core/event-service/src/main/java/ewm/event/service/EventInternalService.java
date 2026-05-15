package ewm.event.service;

import ru.practicum.ewm.internal.dto.EventInternalDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;

import java.util.List;
import java.util.Map;

/**
 * Внутренний сервис предоставления данных о событиях другим микросервисам.
 */
public interface EventInternalService {
    /**
     * Возвращает событие для межсервисных проверок.
     *
     * @param eventId идентификатор события
     * @return внутреннее представление события
     */
    EventInternalDto getEvent(Long eventId);

    /**
     * Возвращает найденные события из переданного списка идентификаторов.
     *
     * @param ids идентификаторы событий
     * @return найденные события
     */
    List<EventInternalDto> getEvents(List<Long> ids);

    /**
     * Проверяет существование событий.
     *
     * @param ids идентификаторы событий
     * @return карта {@code id -> существует}
     */
    Map<Long, Boolean> existsEvents(List<Long> ids);

    /**
     * Возвращает краткие данные событий с именами инициаторов и категорий.
     *
     * @param ids идентификаторы событий
     * @return краткие внутренние представления событий
     */
    List<EventShortInternalDto> getShortEvents(List<Long> ids);

    /**
     * Проверяет наличие событий в категории.
     *
     * @param categoryId идентификатор категории
     * @return {@code true}, если категория используется хотя бы одним событием
     */
    boolean existsByCategory(Long categoryId);
}
