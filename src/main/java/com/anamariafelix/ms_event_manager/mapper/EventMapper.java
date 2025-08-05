package com.anamariafelix.ms_event_manager.mapper;

import com.anamariafelix.ms_event_manager.dto.EventCreateDTO;
import com.anamariafelix.ms_event_manager.dto.EventResponseDTO;
import com.anamariafelix.ms_event_manager.model.Event;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEvent(EventCreateDTO eventCreateDTO){

        return new ModelMapper().map(eventCreateDTO, Event.class);
    }

    public static EventResponseDTO toEventDTO(Event event){

        return new ModelMapper().map(event, EventResponseDTO.class);
    }

    public static List<EventResponseDTO> toListEventDTO(List<Event> events){

        return events.stream().map(event -> toEventDTO(event)).collect(Collectors.toList());
    }
}
