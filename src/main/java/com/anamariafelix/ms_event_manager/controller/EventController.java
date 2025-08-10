package com.anamariafelix.ms_event_manager.controller;

import com.anamariafelix.ms_event_manager.controller.docs.EventControllerDocs;
import com.anamariafelix.ms_event_manager.dto.EventCreateDTO;
import com.anamariafelix.ms_event_manager.dto.EventResponseDTO;
import com.anamariafelix.ms_event_manager.model.Event;
import com.anamariafelix.ms_event_manager.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.anamariafelix.ms_event_manager.mapper.EventMapper.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Event", description = "Endpoints for Managing Events")
public class EventController implements EventControllerDocs {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDTO> create(@RequestBody @Valid EventCreateDTO eventCreateDTO) {
        Event event = eventService.create(toEvent(eventCreateDTO));

        return ResponseEntity.status(201).body(toEventDTO(event));
    }

    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable String id) {
        Event event = eventService.fidById(id);
        return ResponseEntity.ok().body(toEventDTO(event));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventResponseDTO>> findAll() {
        List<Event> events = eventService.fidAll();
        return ResponseEntity.ok().body(toListEventDTO(events));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<List<EventResponseDTO>> findAllSorted() {
        List<Event> events = eventService.fidAllSorted();
        return ResponseEntity.ok().body(toListEventDTO(events));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-event/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable String id, @RequestBody @Valid EventCreateDTO eventCreateDTO) {
        Event event = eventService.update(id, toEvent(eventCreateDTO));
        return ResponseEntity.ok().body(toEventDTO(event));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
