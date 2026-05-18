package ru.practicum.ewm.stats.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "event_similarity")
public class EventSimilarityEntity {

    @EmbeddedId
    private EventSimilarityId id;

    @Column(nullable = false)
    private double score;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected EventSimilarityEntity() {
    }

    public EventSimilarityEntity(EventSimilarityId id, double score, Instant updatedAt) {
        this.id = id;
        this.score = score;
        this.updatedAt = updatedAt;
    }

    public EventSimilarityId getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getOtherEvent(long eventId) {
        if (id.getEventA() == eventId) {
            return id.getEventB();
        }
        if (id.getEventB() == eventId) {
            return id.getEventA();
        }
        throw new IllegalArgumentException("Event " + eventId + " is not part of similarity pair");
    }
}
