package com.anamariafelix.ms_event_manager.client;

import com.anamariafelix.ms_event_manager.client.dto.TicketResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ticket-client", url = "${ms.ticket.url}")
public interface MsTicketClientOpenFeign {

    @GetMapping("/get-ticket-eventId/{eventId}")
    List<TicketResponseDTO> findAllEventId(@PathVariable String eventId) ;
}
