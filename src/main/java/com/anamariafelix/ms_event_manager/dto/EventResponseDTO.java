package com.anamariafelix.ms_event_manager.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EventResponseDTO {

    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String zipCode;
    private String street;
    private String neighborhood;
    private String city;
    private String uf;

}
