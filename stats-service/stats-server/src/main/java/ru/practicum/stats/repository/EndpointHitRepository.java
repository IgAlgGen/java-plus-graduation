package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.EndpointHitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHitEntity, Long> {
    /**
     * Считает все посещения по приложениям и URI за период с необязательным ограничением по URI.
     *
     * @param start начало периода включительно
     * @param end конец периода включительно
     * @param uris URI для фильтрации; {@code null} означает отсутствие фильтра
     * @return статистика, отсортированная по количеству посещений по убыванию
     */
    @Query("select new ru.practicum.ewm.stats.dto.ViewStatsDto(h.app, h.uri, count(h.id)) "
            + "from EndpointHitEntity h "
            + "where h.timestamp between :start and :end "
            + "and (:uris is null or h.uri in :uris) "
            + "group by h.app, h.uri "
            + "order by count(h.id) desc")
    List<ViewStatsDto> findStats(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 @Param("uris") List<String> uris);

    /**
     * Считает уникальные посещения по IP-адресу для каждого приложения и URI.
     *
     * @param start начало периода включительно
     * @param end конец периода включительно
     * @param uris URI для фильтрации; {@code null} означает отсутствие фильтра
     * @return статистика уникальных просмотров, отсортированная по количеству по убыванию
     */
    @Query("select new ru.practicum.ewm.stats.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) "
            + "from EndpointHitEntity h "
            + "where h.timestamp between :start and :end "
            + "and (:uris is null or h.uri in :uris) "
            + "group by h.app, h.uri "
            + "order by count(distinct h.ip) desc")
    List<ViewStatsDto> findStatsUnique(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);
}
