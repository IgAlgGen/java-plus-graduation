package ewm.compilation.mapper;

import ewm.category.dto.CategoryDto;
import ewm.compilation.dto.CompilationDto;
import ewm.compilation.model.Compilation;
import ewm.event.dto.EventShortDto;
import ewm.user.dto.UserShortDto;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;

import java.util.Map;

public class CompilationMapper {

    public static CompilationDto toDto(Compilation c, Map<Long, EventShortInternalDto> events) {
        return new CompilationDto(
                c.getId(),
                c.getTitle(),
                c.getPinned(),
                c.getEventIds().stream()
                        .map(id -> toEventShortDto(events.get(id)))
                        .filter(java.util.Objects::nonNull)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    private static EventShortDto toEventShortDto(EventShortInternalDto event) {
        if (event == null) {
            return null;
        }

        CategoryDto category = new CategoryDto();
        category.setId(event.categoryId());
        category.setName(event.categoryName());

        UserShortDto initiator = new UserShortDto();
        initiator.setId(event.initiatorId());
        initiator.setName(event.initiatorName());

        EventShortDto dto = new EventShortDto();
        dto.setId(event.id());
        dto.setTitle(event.title());
        dto.setAnnotation(event.annotation());
        dto.setCategory(category);
        dto.setInitiator(initiator);
        dto.setEventDate(event.eventDate());
        dto.setPaid(event.paid());
        dto.setViews(0L);
        dto.setConfirmedRequests(0L);
        return dto;
    }
}
