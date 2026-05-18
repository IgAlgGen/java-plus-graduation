package client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.proto.dashboard.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.dashboard.RecommendationsControllerGrpc;
import ru.practicum.ewm.stats.proto.dashboard.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.dashboard.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.dashboard.UserPredictionsRequestProto;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class GrpcAnalyzerClient implements AnalyzerClient {

    @GrpcClient("analyzer")
    private RecommendationsControllerGrpc.RecommendationsControllerBlockingStub analyzerStub;

    @Override
    public List<RecommendedEvent> getRecommendationsForUser(long userId, int maxResults) {
        UserPredictionsRequestProto request = UserPredictionsRequestProto.newBuilder()
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();

        return execute(() -> analyzerStub.getRecommendationsForUser(request));
    }

    @Override
    public List<RecommendedEvent> getSimilarEvents(long eventId, long userId, int maxResults) {
        SimilarEventsRequestProto request = SimilarEventsRequestProto.newBuilder()
                .setEventId(eventId)
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();

        return execute(() -> analyzerStub.getSimilarEvents(request));
    }

    @Override
    public List<RecommendedEvent> getInteractionsCount(Collection<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return List.of();
        }

        InteractionsCountRequestProto request = InteractionsCountRequestProto.newBuilder()
                .addAllEventId(eventIds)
                .build();

        return execute(() -> analyzerStub.getInteractionsCount(request));
    }

    private List<RecommendedEvent> execute(GrpcCall call) {
        try {
            Iterator<RecommendedEventProto> response = call.execute();
            Iterable<RecommendedEventProto> iterable = () -> response;
            return StreamSupport.stream(iterable.spliterator(), false)
                    .map(this::toClientDto)
                    .toList();
        } catch (RuntimeException exception) {
            throw new StatsServerUnavailable("Ошибка запроса рекомендаций в analyzer", exception);
        }
    }

    private RecommendedEvent toClientDto(RecommendedEventProto proto) {
        return new RecommendedEvent(proto.getEventId(), proto.getScore());
    }

    @FunctionalInterface
    private interface GrpcCall {
        Iterator<RecommendedEventProto> execute();
    }
}
