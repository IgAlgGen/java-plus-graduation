package ru.practicum.ewm.stats.analyzer.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.analyzer.service.AnalyzerService;
import ru.practicum.ewm.stats.analyzer.service.RecommendedEvent;
import ru.practicum.ewm.stats.proto.dashboard.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.dashboard.RecommendationsControllerGrpc;
import ru.practicum.ewm.stats.proto.dashboard.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.dashboard.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.dashboard.UserPredictionsRequestProto;

import java.util.List;

@GrpcService
public class RecommendationsGrpcService
        extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final AnalyzerService analyzerService;

    public RecommendationsGrpcService(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @Override
    public void getRecommendationsForUser(UserPredictionsRequestProto request,
                                          StreamObserver<RecommendedEventProto> responseObserver) {
        stream(responseObserver, () -> analyzerService.getRecommendationsForUser(
                request.getUserId(),
                request.getMaxResults()
        ));
    }

    @Override
    public void getSimilarEvents(SimilarEventsRequestProto request,
                                 StreamObserver<RecommendedEventProto> responseObserver) {
        stream(responseObserver, () -> analyzerService.getSimilarEvents(
                request.getEventId(),
                request.getUserId(),
                request.getMaxResults()
        ));
    }

    @Override
    public void getInteractionsCount(InteractionsCountRequestProto request,
                                     StreamObserver<RecommendedEventProto> responseObserver) {
        stream(responseObserver, () -> analyzerService.getInteractionsCount(request.getEventIdList()));
    }

    private void stream(StreamObserver<RecommendedEventProto> responseObserver,
                        RecommendationSupplier supplier) {
        try {
            for (RecommendedEvent recommendation : supplier.get()) {
                responseObserver.onNext(RecommendedEventProto.newBuilder()
                        .setEventId(recommendation.eventId())
                        .setScore(recommendation.score())
                        .build());
            }
            responseObserver.onCompleted();
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(exception.getMessage())
                    .withCause(exception)
                    .asRuntimeException());
        } catch (RuntimeException exception) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to build recommendations")
                    .withCause(exception)
                    .asRuntimeException());
        }
    }

    @FunctionalInterface
    private interface RecommendationSupplier {
        List<RecommendedEvent> get();
    }
}
