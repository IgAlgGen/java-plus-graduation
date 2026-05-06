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
                .event(r.getEvent().getId())
                .requester(r.getRequester().getUserId())
                .status(r.getStatus().name())
                .build();
    }

    public static ParticipationRequestInternalDto toInternalDto(ParticipationRequest r) {
        return new ParticipationRequestInternalDto(
                r.getId(),
                r.getCreated(),
                r.getEvent().getId(),
                r.getRequester().getUserId(),
                r.getStatus().name()
        );
    }
}
