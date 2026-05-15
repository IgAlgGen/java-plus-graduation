package ewm.request.service;

import client.ActionType;
import client.CollectorClient;
import ewm.common.exception.ConflictException;
import ewm.common.exception.NotFoundException;
import ewm.event.service.EventReferenceService;
import ewm.request.dto.EventRequestStatusUpdateRequest;
import ewm.request.dto.EventRequestStatusUpdateResult;
import ewm.request.dto.ParticipationRequestDto;
import ewm.request.mapper.ParticipationRequestMapper;
import ewm.request.model.ParticipationRequest;
import ewm.request.model.RequestStatus;
import ewm.request.repository.ParticipationRequestRepository;
import ewm.user.service.UserReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import ru.practicum.ewm.internal.dto.EventInternalDto;

/**
 * Реализация правил подачи и модерации заявок на участие.
 *
 * <p>Сервис проверяет публикацию события, запрет заявок от инициатора, уникальность заявки
 * и лимит участников с учетом автоматического подтверждения.</p>
 */
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepo;
    private final UserReferenceService userReferenceService;
    private final EventReferenceService eventReferenceService;
    private final CollectorClient collectorClient;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        ensureUserExists(userId);
        EventInternalDto eventData = eventReferenceService.getExistingEvent(eventId);

        if (Objects.equals(eventData.initiatorId(), userId)) {
            throw new ConflictException("Инициатор не может подать заявку на участие в своем событии");
        }
        if (!"PUBLISHED".equals(eventData.state())) {
            throw new ConflictException("Событие не опубликовано");
        }
        if (requestRepo.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Заявка на участие уже существует");
        }

        long limit = eventData.participantLimit();
        long confirmed = requestRepo.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        if (limit > 0 && confirmed >= limit) {
            throw new ConflictException("Достигнут лимит участников");
        }

        ParticipationRequest req = new ParticipationRequest();
        req.setRequesterId(userId);
        req.setEventId(eventId);

        RequestStatus status;
        if (eventData.participantLimit() == 0 || Boolean.FALSE.equals(eventData.requestModeration())) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }
        req.setStatus(status);

        // если автоподтверждение и лимит > 0 — повторная защита от гонки
        if (status == RequestStatus.CONFIRMED && limit > 0) {
            long confirmedAfter = requestRepo.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (confirmedAfter >= limit) {
                throw new ConflictException("Достигнут лимит участников");
            }
        }

        ParticipationRequest saved = requestRepo.save(req);
        collectorClient.collectUserAction(userId, eventId, ActionType.REGISTER);
        return ParticipationRequestMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        ParticipationRequest req = requestRepo.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Заявка с id=" + requestId + " не найдена"));

        if (req.getStatus() == RequestStatus.CONFIRMED) {
        }

        req.setStatus(RequestStatus.CANCELED);
        return ParticipationRequestMapper.toDto(req);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        ensureUserExists(userId);
        return requestRepo.findAllByRequesterId(userId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        EventInternalDto eventData = eventReferenceService.getExistingEvent(eventId);
        if (!Objects.equals(eventData.initiatorId(), userId)) {
            throw new ConflictException("Только инициатор может просматривать заявки на событие");
        }
        return requestRepo.findAllByEventId(eventId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(
            Long userId,
            Long eventId,
            List<Long> requestIds,
            EventRequestStatusUpdateRequest.RequestUpdateStatus status
    ) {
        EventInternalDto eventData = eventReferenceService.getExistingEvent(eventId);

        if (!Objects.equals(eventData.initiatorId(), userId)) {
            throw new ConflictException("Только инициатор может изменять заявки на событие");
        }

        List<ParticipationRequest> requests =
                requestRepo.findAllByIdInAndEventId(requestIds, eventId);

        if (requests.size() != new HashSet<>(requestIds).size()) {
            throw new NotFoundException("Некоторые заявки для события не найдены");
        }

        for (ParticipationRequest r : requests) {
            if (r.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Можно изменять только заявки в статусе ожидания");
            }
        }

        List<ParticipationRequest> confirmedOut = new ArrayList<>();
        List<ParticipationRequest> rejectedOut = new ArrayList<>();

        if (status == EventRequestStatusUpdateRequest.RequestUpdateStatus.REJECTED) {
            for (ParticipationRequest r : requests) {
                r.setStatus(RequestStatus.REJECTED);
                rejectedOut.add(r);
            }
            return toResult(confirmedOut, rejectedOut);
        }

        // status == CONFIRMED
        long limit = eventData.participantLimit();
        long confirmed = requestRepo.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        if (limit > 0 && confirmed >= limit) {
            throw new ConflictException("Достигнут лимит участников");
        }

        long slots = (limit == 0) ? Long.MAX_VALUE : (limit - confirmed);

        for (ParticipationRequest r : requests) {
            if (slots > 0) {
                r.setStatus(RequestStatus.CONFIRMED);
                confirmedOut.add(r);
                slots--;
                confirmed++;
            } else {
                r.setStatus(RequestStatus.REJECTED);
                rejectedOut.add(r);
            }
        }

        return toResult(confirmedOut, rejectedOut);
    }

    private EventRequestStatusUpdateResult toResult(List<ParticipationRequest> confirmed,
                                                    List<ParticipationRequest> rejected) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed.stream().map(ParticipationRequestMapper::toDto).toList())
                .rejectedRequests(rejected.stream().map(ParticipationRequestMapper::toDto).toList())
                .build();
    }

    private void ensureUserExists(Long userId) {
        userReferenceService.ensureExists(userId);
    }
}
