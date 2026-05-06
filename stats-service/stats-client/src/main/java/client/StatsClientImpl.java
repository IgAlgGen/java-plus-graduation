package client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class StatsClientImpl implements StatsClient {

    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private final RetryTemplate retryTemplate;
    private final String statsServiceId;

    public StatsClientImpl(
            DiscoveryClient discoveryClient,
            @Value("${stats.service.id:stats-server}") String statsServiceId
    ) {
        this.restTemplate = this.getRestTemplate();
        this.discoveryClient = discoveryClient;
        this.retryTemplate = this.getRetryTemplate();
        this.statsServiceId = statsServiceId;
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private RetryTemplate getRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        MaxAttemptsRetryPolicy retryPolicy = new MaxAttemptsRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    private ServiceInstance getInstance() {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances(statsServiceId);
            if (instances.isEmpty()) {
                throw new StatsServerUnavailable("Сервис статистики не найден в Eureka с id: " + statsServiceId);
            }
            return instances.get(0);
        } catch (StatsServerUnavailable exception) {
            throw exception;
        } catch (Exception exception) {
            throw new StatsServerUnavailable(
                    "Ошибка обнаружения адреса сервиса статистики с id: " + statsServiceId,
                    exception
            );
        }
    }

    private URI makeUri(String path) {
        ServiceInstance instance = retryTemplate.execute(context -> getInstance());
        return URI.create("http://" + instance.getHost() + ":" + instance.getPort() + path);
    }

    /**
     * POST /hit
     */
    @Override
    public void hit(EndpointHitDto endpointHit) {
        URI uri = makeUri("/hit");

        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHit);
        restTemplate.postForEntity(uri, request, Void.class);
    }

    /**
     * GET /stats
     */
    @Override
    public List<ViewStatsDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUri(makeUri("/stats"))
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter));

        if (uris != null && !uris.isEmpty()) {
            uris.forEach(uri -> builder.queryParam("uris", uri));
        }

        if (unique != null) {
            builder.queryParam("unique", unique);
        }

        URI uri = builder.build().encode().toUri();
        log.info("getStats URI: {}", uri);

        ResponseEntity<List<ViewStatsDto>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody() != null
                ? response.getBody()
                : Collections.emptyList();
    }
}
