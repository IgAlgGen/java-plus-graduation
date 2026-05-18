package ru.practicum.ewm.stats.analyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stats.kafka.topic")
public class AnalyzerKafkaProperties {

    private String userActions;
    private String eventsSimilarity;

    public String getUserActions() {
        return userActions;
    }

    public void setUserActions(String userActions) {
        this.userActions = userActions;
    }

    public String getEventsSimilarity() {
        return eventsSimilarity;
    }

    public void setEventsSimilarity(String eventsSimilarity) {
        this.eventsSimilarity = eventsSimilarity;
    }
}
