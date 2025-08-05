package com.anamariafelix.ms_event_manager.service;

import com.anamariafelix.ms_event_manager.dto.ViaCepResponseDTO;
import com.anamariafelix.ms_event_manager.exception.EventNotFoundException;
import com.anamariafelix.ms_event_manager.infra.ViaCepClientOpenFeign;
import com.anamariafelix.ms_event_manager.model.Event;
import com.anamariafelix.ms_event_manager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClientOpenFeign viaCepClient;

    @Transactional
    public Event create(Event event) {
        ViaCepResponseDTO enderecoViaCep = viaCepClient.findByAddress(event.getCep());

        if (enderecoViaCep != null) {
            event.setLogradouro(enderecoViaCep.getLogradouro());
            event.setBairro(enderecoViaCep.getBairro());
            event.setCidade(enderecoViaCep.getLocalidade());
            event.setUf(enderecoViaCep.getUf());
        }

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
}
