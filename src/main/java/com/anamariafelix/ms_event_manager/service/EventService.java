package com.anamariafelix.ms_event_manager.service;

import com.anamariafelix.ms_event_manager.client.MsTicketClientOpenFeign;
import com.anamariafelix.ms_event_manager.client.dto.TicketResponseDTO;
import com.anamariafelix.ms_event_manager.dto.ViaCepResponseDTO;
import com.anamariafelix.ms_event_manager.enums.Status;
import com.anamariafelix.ms_event_manager.exception.OpenFeignConnectionException;
import com.anamariafelix.ms_event_manager.exception.*;
import com.anamariafelix.ms_event_manager.client.ViaCepClientOpenFeign;
import com.anamariafelix.ms_event_manager.model.Event;
import com.anamariafelix.ms_event_manager.repository.EventRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClientOpenFeign viaCepClient;
    private final MsTicketClientOpenFeign msTicketClientOpenFeign;

    @Transactional
    public Event create(Event event){

        Event existingEvent = eventRepository.findByEventNameAndDateTimeAndDeletedFalse(event.getEventName(), event.getDateTime());

        if (existingEvent != null) {
            throw new EventConflictException(String.format("Event '%s' on date '%s' already registered!", event.getEventName(), event.getDateTime()));
        }

        ViaCepResponseDTO addressViaCep = viaCepClient.findByAddress(event.getZipCode());

        if (Objects.isNull(addressViaCep) || Boolean.TRUE.equals(addressViaCep.getError())) {
            throw new ViaCepNullException(String.format("Invalid zip code %s, please provide a valid zip code!!", event.getZipCode()));
        }

        event.setStreet(addressViaCep.getStreet());
        event.setNeighborhood(addressViaCep.getNeighborhood());
        event.setCity(addressViaCep.getCity());
        event.setUf(addressViaCep.getState());

        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Event fidById(String id){
        return eventRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id = %s not found!", id)));
    }

    @Transactional(readOnly = true)
    public List<Event> fidAll() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Event> fidAllSorted() {
        return eventRepository.findAllByDeletedFalseOrderByEventNameAsc();
    }

    @Transactional
    public Event update(String id, Event eventUpdate) {
        Event event = fidById(id);

        if(!event.getEventName().equals(eventUpdate.getEventName())){
            event.setEventName(eventUpdate.getEventName());
        }
        if(!event.getDateTime().equals(eventUpdate.getDateTime())){
            event.setDateTime(eventUpdate.getDateTime());
        }
        if(!event.getZipCode().equals(eventUpdate.getZipCode())){

            ViaCepResponseDTO addressViaCep = viaCepClient.findByAddress(eventUpdate.getZipCode());

            if (Objects.isNull(addressViaCep) || Boolean.TRUE.equals(addressViaCep.getError())) {
                throw new ViaCepNullException(String.format("Invalid zip code %s, please provide a valid zip code!!",event.getZipCode()));
            }

            event.setZipCode(eventUpdate.getZipCode());

            event.setStreet(addressViaCep.getStreet());
            event.setNeighborhood(addressViaCep.getNeighborhood());
            event.setCity(addressViaCep.getCity());
            event.setUf(addressViaCep.getState());
        }
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteById(String id) {
        try{
            Event event = eventRepository.findByIdAndDeletedFalse(id).orElseThrow(
                    () -> new EventNotFoundException(String.format("Event with id = %s not found!", id)));

            List<TicketResponseDTO> tickets = msTicketClientOpenFeign.findAllEventId(event.getId());

            if(!tickets.isEmpty()){
                throw new EventWithTicketsSoldException("Event With Tickets Sold!");
            }

            event.setDeleted(true);
            event.setDeletedAt(LocalDateTime.now());
            event.setStatus(Status.INACTIVE);
            eventRepository.save(event);
        }catch (FeignException e) {
            throw new OpenFeignConnectionException("Error communicating with event service.");
        }
    }
}
