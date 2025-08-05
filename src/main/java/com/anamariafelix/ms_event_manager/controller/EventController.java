package com.anamariafelix.ms_event_manager.controller;

import com.anamariafelix.ms_event_manager.dto.EventCreateDTO;
import com.anamariafelix.ms_event_manager.dto.EventResponseDTO;
import com.anamariafelix.ms_event_manager.model.Event;
import com.anamariafelix.ms_event_manager.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.anamariafelix.ms_event_manager.mapper.EventMapper.toEvent;
import static com.anamariafelix.ms_event_manager.mapper.EventMapper.toEventDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDTO> create(@RequestBody @Valid EventCreateDTO eventCreateDTO) {
        Event event = eventService.create(toEvent(eventCreateDTO));

        return ResponseEntity.status(201).body(toEventDTO(event));
    }

}
