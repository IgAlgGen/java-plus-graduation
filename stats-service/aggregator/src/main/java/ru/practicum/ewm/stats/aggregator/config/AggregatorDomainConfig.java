package ru.practicum.ewm.stats.aggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.stats.aggregator.EventSimilarityCalculator;

@Configuration
public class AggregatorDomainConfig {

    @Bean
    public EventSimilarityCalculator eventSimilarityCalculator() {
        return new EventSimilarityCalculator();
    }
}
