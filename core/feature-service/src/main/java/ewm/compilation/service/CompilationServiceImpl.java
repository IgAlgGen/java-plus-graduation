package ewm.compilation.service;

import ewm.common.exception.NotFoundException;
import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;
import ewm.compilation.mapper.CompilationMapper;
import ewm.compilation.model.Compilation;
import ewm.compilation.repository.CompilationRepository;
import ewm.event.client.EventClient;
import ewm.event.service.EventReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import ru.practicum.ewm.internal.dto.EventShortInternalDto;
import ru.practicum.ewm.internal.dto.IdsRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventReferenceService eventReferenceService;
    private final EventClient eventClient;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        Compilation comp = new Compilation();
        comp.setTitle(dto.getTitle());
        comp.setPinned(dto.getPinned() != null ? dto.getPinned() : false);

        Set<Long> eventIds = loadEventIds(dto.getEvents());
        comp.setEventIds(eventIds);

        Compilation saved = compilationRepository.save(comp);
        return mapCompilation(saved);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));

        if (dto.getTitle() != null) {
            comp.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            comp.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null) {
            comp.setEventIds(loadEventIds(dto.getEvents()));
        }

        Compilation saved = compilationRepository.save(comp);
        return mapCompilation(saved);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation not found: " + compId);
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Compilation> comps = (pinned == null)
                ? compilationRepository.findAll(pageable).getContent()
                : compilationRepository.findAllByPinned(pinned, pageable);

        Map<Long, EventShortInternalDto> events = eventsById(comps);
        return comps.stream()
                .map(comp -> CompilationMapper.toDto(comp, events))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto findById(Long compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + compId));
        return mapCompilation(comp);
    }

    private Set<Long> loadEventIds(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return new HashSet<>();
        }

        eventReferenceService.ensureEventsExist(eventIds);
        return new HashSet<>(eventIds);
    }

    private CompilationDto mapCompilation(Compilation compilation) {
        return CompilationMapper.toDto(compilation, eventsById(List.of(compilation)));
    }

    private Map<Long, EventShortInternalDto> eventsById(List<Compilation> compilations) {
        List<Long> ids = compilations.stream()
                .flatMap(compilation -> compilation.getEventIds().stream())
                .distinct()
                .toList();

        if (ids.isEmpty()) {
            return Map.of();
        }

        return eventClient.getShortEvents(new IdsRequest(ids)).stream()
                .collect(Collectors.toMap(EventShortInternalDto::id, Function.identity()));
    }
}
