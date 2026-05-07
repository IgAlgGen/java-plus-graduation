package ewm.event.service;

import ewm.event.dto.*;
import ewm.event.model.EventSort;
import ewm.event.model.EventState;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис пользовательских, публичных и административных операций с событиями.
 */
public interface EventService {
    /**
     * Создает событие от имени пользователя.
     *
     * @param userId идентификатор инициатора события
     * @param eventDto данные нового события
     * @return созданное событие
     * @throws ewm.common.exception.BadRequestException если дата события раньше допустимого срока
     * @throws ewm.common.exception.NotFoundException если пользователь или категория не найдены
     */
    EventFullDto create(Long userId, NewEventDto eventDto);

    /**
     * Возвращает событие инициатора.
     *
     * @param userId идентификатор инициатора
     * @param eventId идентификатор события
     * @return полная информация о событии
     * @throws ewm.common.exception.NotFoundException если событие не найдено или принадлежит другому пользователю
     */
    EventFullDto get(Long userId, Long eventId);

    /**
     * Выполняет административный поиск событий по необязательным фильтрам.
     *
     * @param users идентификаторы инициаторов
     * @param states состояния событий
     * @param categories идентификаторы категорий
     * @param rangeStart начало диапазона дат событий
     * @param rangeEnd конец диапазона дат событий
     * @param from смещение первого результата
     * @param size размер страницы
     * @return список событий с полной информацией
     */
    List<EventFullDto> get(List<Long> users,
                           List<EventState> states,
                           List<Long> categories,
                           LocalDateTime rangeStart,
                           LocalDateTime rangeEnd,
                           int from,
                           int size);

    /**
     * Возвращает опубликованное событие и регистрирует просмотр в сервисе статистики.
     *
     * @param eventId идентификатор события
     * @param request HTTP-запрос, из которого берутся URI и IP клиента
     * @return опубликованное событие
     * @throws ewm.common.exception.NotFoundException если событие не найдено или не опубликовано
     */
    EventFullDto getPublicEvent(Long eventId, HttpServletRequest request);

    /**
     * Возвращает события, созданные пользователем.
     *
     * @param userId идентификатор пользователя
     * @param from смещение первого результата
     * @param size размер страницы
     * @return краткая информация о событиях пользователя
     */
    List<EventShortDto> getEvents(Long userId, int from, int size);

    /**
     * Выполняет публичный поиск только среди опубликованных событий и регистрирует просмотр списка.
     *
     * @param text текст для поиска в аннотации и описании
     * @param categories категории событий
     * @param paid фильтр платности
     * @param rangeStart начало диапазона дат; если не задано, используется текущее время
     * @param rangeEnd конец диапазона дат
     * @param onlyAvailable если {@code true}, возвращаются события со свободными местами
     * @param sort способ сортировки результата
     * @param from смещение первого результата
     * @param size размер страницы
     * @param request HTTP-запрос, из которого берутся URI и IP клиента
     * @return краткая информация о найденных событиях
     * @throws ewm.common.exception.BadRequestException если конец диапазона раньше начала
     */
    List<EventShortDto> getPublicEvents(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        EventSort sort,
                                        int from,
                                        int size,
                                        HttpServletRequest request);

    /**
     * Обновляет событие инициатором.
     *
     * @param userId идентификатор инициатора
     * @param eventId идентификатор события
     * @param updateEventUserRequest изменяемые поля
     * @return обновленное событие
     * @throws ewm.common.exception.ConflictException если событие уже опубликовано
     * @throws ewm.common.exception.BadRequestException если пользователь не является инициатором или дата некорректна
     */
    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    /**
     * Обновляет событие администратором, включая публикацию или отклонение.
     *
     * @param eventId идентификатор события
     * @param updateEventAdminRequest изменяемые поля и административное действие
     * @return обновленное событие
     * @throws ewm.common.exception.ConflictException если нарушены правила публикации или перехода состояния
     * @throws ewm.common.exception.NotFoundException если событие не найдено
     */
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
