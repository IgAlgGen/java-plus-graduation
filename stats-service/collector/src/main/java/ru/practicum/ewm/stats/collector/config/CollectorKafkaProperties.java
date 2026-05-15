package ru.practicum.ewm.stats.collector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stats.kafka.topic")
public class CollectorKafkaProperties {

    private String userActions;

    public String getUserActions() {
        return userActions;
    }

    public void setUserActions(String userActions) {
        this.userActions = userActions;
    }
}
