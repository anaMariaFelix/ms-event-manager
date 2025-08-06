package com.anamariafelix.ms_event_manager.service;

import com.anamariafelix.ms_event_manager.dto.ViaCepResponseDTO;
import com.anamariafelix.ms_event_manager.exception.EventConflictException;
import com.anamariafelix.ms_event_manager.exception.EventNotFoundException;
import com.anamariafelix.ms_event_manager.exception.ViaCepNullException;
import com.anamariafelix.ms_event_manager.infra.ViaCepClientOpenFeign;
import com.anamariafelix.ms_event_manager.model.Event;
import com.anamariafelix.ms_event_manager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClientOpenFeign viaCepClient;

    @Transactional
    public Event create(Event event){
        Event existingEvent = eventRepository.findByEventNameAndDateTime(event.getEventName(), event.getDateTime());

        if (existingEvent != null) {
            throw new EventConflictException(String.format("Event '%s' on date '%s' already registered!",event.getEventName(), event.getDateTime()));
        }

        ViaCepResponseDTO enderecoViaCep = viaCepClient.findByAddress(event.getCep());

        if (Objects.isNull(enderecoViaCep) || Boolean.TRUE.equals(enderecoViaCep.getErro())) {
            throw new ViaCepNullException(String.format("Invalid zip code %s, please provide a valid zip code!!",event.getCep()));
        }
        event.setLogradouro(enderecoViaCep.getLogradouro());
        event.setBairro(enderecoViaCep.getBairro());
        event.setCidade(enderecoViaCep.getLocalidade());
        event.setUf(enderecoViaCep.getUf());

        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Event fidById(String id){
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id = %s not found!", id)));
    }

    @Transactional(readOnly = true)
    public List<Event> fidAll() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Event> fidAllSorted() {
        return eventRepository.findAllByOrderByEventNameAsc();
    }

    @Transactional
    public Event update(String id, Event eventUpdate) {
        Event event = fidById(id);

        if(event.getEventName() != eventUpdate.getEventName()){
            event.setEventName(eventUpdate.getEventName());
        }
        if(event.getDateTime() != eventUpdate.getDateTime()){
            event.setDateTime(eventUpdate.getDateTime());
        }
        if(event.getCep() != eventUpdate.getCep()){
            event.setCep(eventUpdate.getCep());

            ViaCepResponseDTO enderecoViaCep = viaCepClient.findByAddress(event.getCep());

            if (enderecoViaCep != null) {
                event.setLogradouro(enderecoViaCep.getLogradouro());
                event.setBairro(enderecoViaCep.getBairro());
                event.setCidade(enderecoViaCep.getLocalidade());
                event.setUf(enderecoViaCep.getUf());
            }
        }
        return eventRepository.save(event);
    }
}
