package ru.practicum.ewm.stats.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventSimilarityId implements Serializable {

    @Column(name = "event_a", nullable = false)
    private Long eventA;

    @Column(name = "event_b", nullable = false)
    private Long eventB;

    protected EventSimilarityId() {
    }

    public EventSimilarityId(long eventA, long eventB) {
        if (eventA == eventB) {
            throw new IllegalArgumentException("Similarity pair must contain different events");
        }
        this.eventA = Math.min(eventA, eventB);
        this.eventB = Math.max(eventA, eventB);
    }

    public Long getEventA() {
        return eventA;
    }

    public Long getEventB() {
        return eventB;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EventSimilarityId that)) {
            return false;
        }
        return Objects.equals(eventA, that.eventA) && Objects.equals(eventB, that.eventB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventA, eventB);
    }
}
