package com.anamariafelix.ms_event_manager.service.event;

import com.anamariafelix.ms_event_manager.client.MsTicketClientOpenFeign;
import com.anamariafelix.ms_event_manager.client.ViaCepClientOpenFeign;
import com.anamariafelix.ms_event_manager.client.dto.TicketResponseDTO;
import com.anamariafelix.ms_event_manager.client.dto.ViaCepResponseDTO;
import com.anamariafelix.ms_event_manager.enums.Status;
import com.anamariafelix.ms_event_manager.exception.*;
import com.anamariafelix.ms_event_manager.model.Event;
import com.anamariafelix.ms_event_manager.repository.EventRepository;
import com.anamariafelix.ms_event_manager.service.EventService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ViaCepClientOpenFeign viaCepClient;

    @Mock
    private MsTicketClientOpenFeign msTicketClientOpenFeign;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private Event eventUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        event = new Event();
        event.setEventName("Show");
        event.setDateTime(LocalDateTime.of(2025, 8, 20, 20, 0));
        event.setZipCode("01001000");

        eventUpdate = new Event();
        eventUpdate.setEventName("Movie X");
        eventUpdate.setDateTime(LocalDateTime.of(2025, 9, 15, 18, 0));
        eventUpdate.setZipCode("02020200");
    }

    @Test
    void create_ShouldThrowEventConflictException_WhenEventAlreadyExists() {
        when(eventRepository.findByEventNameAndDateTimeAndDeletedFalse(
                event.getEventName(), event.getDateTime()))
                .thenReturn(new Event());

        EventConflictException ex = assertThrows(EventConflictException.class, () -> {
            eventService.create(event);
        });

        assertTrue(ex.getMessage().contains("already registered"));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowViaCepNullException_WhenCepIsInvalid() {
        when(eventRepository.findByEventNameAndDateTimeAndDeletedFalse(
                event.getEventName(), event.getDateTime()))
                .thenReturn(null);

        when(viaCepClient.findByAddress(event.getZipCode())).thenReturn(null);

        ViaCepNullException ex = assertThrows(ViaCepNullException.class, () -> {
            eventService.create(event);
        });

        assertTrue(ex.getMessage().contains("Invalid zip code"));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowViaCepNullException_WhenCepReturnsErrorTrue() {
        when(eventRepository.findByEventNameAndDateTimeAndDeletedFalse(
                event.getEventName(), event.getDateTime()))
                .thenReturn(null);

        ViaCepResponseDTO response = new ViaCepResponseDTO();
        response.setError(true);

        when(viaCepClient.findByAddress(event.getZipCode())).thenReturn(response);

        assertThrows(ViaCepNullException.class, () -> {
            eventService.create(event);
        });

        verify(eventRepository, never()).save(any());
    }

    @Test
    void create_ShouldSaveEvent_WhenValidData() {
        when(eventRepository.findByEventNameAndDateTimeAndDeletedFalse(
                event.getEventName(), event.getDateTime()))
                .thenReturn(null);

        ViaCepResponseDTO response = new ViaCepResponseDTO();
        response.setStreet("7 de setembro");
        response.setNeighborhood("Ipiranga");
        response.setCity("São José do Egito");
        response.setState("PE");
        response.setError(false);

        when(viaCepClient.findByAddress(event.getZipCode())).thenReturn(response);
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event savedEvent = eventService.create(event);

        assertEquals("7 de setembro", savedEvent.getStreet());
        assertEquals("Ipiranga", savedEvent.getNeighborhood());
        assertEquals("São José do Egito", savedEvent.getCity());
        assertEquals("PE", savedEvent.getUf());

        verify(eventRepository, times(1)).save(any(Event.class));
    }


    @Test //findById com id valido
    void fidById_shouldReturnEvent_whenExistsAndNotDeleted() {
        String id = "123";
        Event event = new Event();
        event.setId(id);
        event.setDeleted(false);

        when(eventRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(event));

        Event result = eventService.fidById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(eventRepository).findByIdAndDeletedFalse(id);
    }

    @Test //findById com id invalido
    void fidById_shouldThrowException_whenEventNotFound() {
        String id = "456";
        when(eventRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        EventNotFoundException exception = assertThrows(EventNotFoundException.class,
                () -> eventService.fidById(id));

        assertTrue(exception.getMessage().contains("Event with id = " + id + " not found!"));
        verify(eventRepository).findByIdAndDeletedFalse(id);
    }

    @Test //findAll
    void fidAll_shouldReturnPageOfEvents() {
        Pageable pageable = PageRequest.of(0, 5);
        Event event1 = new Event();
        event1.setId("1");
        Event event2 = new Event();
        event2.setId("2");

        Page<Event> mockPage = new PageImpl<>(List.of(event1, event2), pageable, 2);

        when(eventRepository.findAllByDeletedFalse(pageable)).thenReturn(mockPage);

        Page<Event> result = eventService.fidAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("1", result.getContent().get(0).getId());
        assertEquals("2", result.getContent().get(1).getId());
        verify(eventRepository).findAllByDeletedFalse(pageable);
    }

    //update
    @Test
    void update_ShouldThrowEventNotFoundException_WhenIdNotFound() {
        when(eventRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> {
            eventService.update("1", eventUpdate);
        });

        verify(eventRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateOnlyName_WhenOnlyNameIsDifferent() {
        eventUpdate.setDateTime(event.getDateTime());
        eventUpdate.setZipCode(event.getZipCode());

        when(eventRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event result = eventService.update("1", eventUpdate);

        assertEquals("Movie X", result.getEventName());
        assertEquals(event.getDateTime(), result.getDateTime());
        assertEquals(event.getZipCode(), result.getZipCode());
        verify(viaCepClient, never()).findByAddress(any());
    }

    @Test
    void update_ShouldUpdateOnlyDate_WhenOnlyDateIsDifferent() {
        eventUpdate.setEventName(event.getEventName());
        eventUpdate.setZipCode(event.getZipCode());

        when(eventRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event result = eventService.update("1", eventUpdate);

        assertEquals(event.getEventName(), result.getEventName());
        assertEquals(eventUpdate.getDateTime(), result.getDateTime());
        verify(viaCepClient, never()).findByAddress(any());
    }

    @Test
    void update_ShouldThrowViaCepNullException_WhenNewZipCodeIsInvalid_NullResponse() {
        when(eventRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(event));
        when(viaCepClient.findByAddress("02020200")).thenReturn(null);

        assertThrows(ViaCepNullException.class, () -> {
            eventService.update("1", eventUpdate);
        });

        verify(eventRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowViaCepNullException_WhenNewZipCodeHasErrorTrue() {
        when(eventRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(event));

        ViaCepResponseDTO viaCepResponse = new ViaCepResponseDTO();
        viaCepResponse.setError(true);

        when(viaCepClient.findByAddress("02020200")).thenReturn(viaCepResponse);

        assertThrows(ViaCepNullException.class, () -> {
            eventService.update("1", eventUpdate);
        });

        verify(eventRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateAllFields_WhenValidNewZipCode() {
        when(eventRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(event));

        ViaCepResponseDTO viaCepResponse = new ViaCepResponseDTO();
        viaCepResponse.setStreet("Rua Nova");
        viaCepResponse.setNeighborhood("Centro");
        viaCepResponse.setCity("São José do egito");
        viaCepResponse.setState("PE");
        viaCepResponse.setError(false);

        when(viaCepClient.findByAddress("02020200")).thenReturn(viaCepResponse);
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event result = eventService.update("1", eventUpdate);

        assertEquals("Movie X", result.getEventName());
        assertEquals(eventUpdate.getDateTime(), result.getDateTime());
        assertEquals("Rua Nova", result.getStreet());
        assertEquals("Centro", result.getNeighborhood());
        assertEquals("São José do egito", result.getCity());
        assertEquals("PE", result.getUf());
        verify(viaCepClient, times(1)).findByAddress("02020200");
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    //delete
    @Test
    void deleteById_shouldThrowEventNotFoundException_whenEventDoesNotExist() {
        String eventId = "123";
        when(eventRepository.findByIdAndDeletedFalse(eventId))
                .thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.deleteById(eventId));

        verify(eventRepository).findByIdAndDeletedFalse(eventId);
        verifyNoMoreInteractions(eventRepository, msTicketClientOpenFeign);
    }

    @Test
    void deleteById_shouldThrowEventWithTicketsSoldException_whenTicketsExist() {
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);

        when(eventRepository.findByIdAndDeletedFalse(eventId))
                .thenReturn(Optional.of(event));
        when(msTicketClientOpenFeign.findAllEventId(eventId))
                .thenReturn(List.of(new TicketResponseDTO()));

        assertThrows(EventWithTicketsSoldException.class, () -> eventService.deleteById(eventId));

        verify(msTicketClientOpenFeign).findAllEventId(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    void deleteById_shouldMarkEventAsDeleted_whenNoTicketsExist() {
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);

        when(eventRepository.findByIdAndDeletedFalse(eventId))
                .thenReturn(Optional.of(event));
        when(msTicketClientOpenFeign.findAllEventId(eventId))
                .thenReturn(Collections.emptyList());

        eventService.deleteById(eventId);

        assertTrue(event.isDeleted());
        assertNotNull(event.getDeletedAt());
        assertEquals(Status.INACTIVE, event.getStatus());
        verify(eventRepository).save(event);
    }

    @Test
    void deleteById_shouldThrowOpenFeignConnectionException_whenFeignFails() {
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);

        when(eventRepository.findByIdAndDeletedFalse(eventId))
                .thenReturn(Optional.of(event));
        when(msTicketClientOpenFeign.findAllEventId(eventId))
                .thenThrow(FeignException.class);

        assertThrows(OpenFeignConnectionException.class, () -> eventService.deleteById(eventId));
    }
}
