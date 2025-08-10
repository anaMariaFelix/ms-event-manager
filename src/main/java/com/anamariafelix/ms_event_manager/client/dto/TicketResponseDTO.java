package com.anamariafelix.ms_event_manager.client.dto;

import com.anamariafelix.ms_event_manager.client.enums.Status;
import com.anamariafelix.ms_event_manager.model.Event;

public class TicketResponseDTO {

    private String ticketId;

    private String cpf;

    private String customerName;

    private String customerMail;

    private Event event;

    private String BRLTotalAmount;

    private String USDTotalAmount;

    private Status status;
}
