package ewm.request.mapper;

import ewm.request.dto.ParticipationRequestDto;
import ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.internal.dto.ParticipationRequestInternalDto;

public final class ParticipationRequestMapper {

    private ParticipationRequestMapper() {
    }

    public static ParticipationRequestDto toDto(ParticipationRequest r) {
        return ParticipationRequestDto.builder()
                .id(r.getId())
                .created(r.getCreated())
                .event(getEventId(r))
                .requester(getRequesterId(r))
                .status(r.getStatus().name())
                .build();
    }

    public static ParticipationRequestInternalDto toInternalDto(ParticipationRequest r) {
        return new ParticipationRequestInternalDto(
                r.getId(),
                r.getCreated(),
                getEventId(r),
                getRequesterId(r),
                r.getStatus().name()
        );
    }

    public static Long getEventId(ParticipationRequest r) {
        return r.getEventId();
    }

    public static Long getRequesterId(ParticipationRequest r) {
        return r.getRequesterId();
    }
}
