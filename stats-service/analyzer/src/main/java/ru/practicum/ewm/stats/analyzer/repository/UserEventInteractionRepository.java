package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.analyzer.model.UserEventInteractionEntity;
import ru.practicum.ewm.stats.analyzer.model.UserEventInteractionId;

import java.util.Collection;
import java.util.List;

public interface UserEventInteractionRepository
        extends JpaRepository<UserEventInteractionEntity, UserEventInteractionId> {

    List<UserEventInteractionEntity> findByIdUserIdOrderByUpdatedAtDesc(long userId);

    @Query("""
            select interaction.id.eventId, sum(interaction.weight)
            from UserEventInteractionEntity interaction
            where interaction.id.eventId in :eventIds
            group by interaction.id.eventId
            """)
    List<Object[]> sumWeightsByEventIds(Collection<Long> eventIds);
}
