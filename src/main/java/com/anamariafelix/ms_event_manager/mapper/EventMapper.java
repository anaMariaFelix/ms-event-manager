package com.anamariafelix.ms_event_manager.mapper;

import com.anamariafelix.ms_event_manager.dto.EventCreateDTO;
import com.anamariafelix.ms_event_manager.dto.EventResponseDTO;
import com.anamariafelix.ms_event_manager.model.Event;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEvent(EventCreateDTO eventCreateDTO){

        return new ModelMapper().map(eventCreateDTO, Event.class);
    }

    public static EventResponseDTO toEventDTO(Event event){

        return new ModelMapper().map(event, EventResponseDTO.class);
    }
}
