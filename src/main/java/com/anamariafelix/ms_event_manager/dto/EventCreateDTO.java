package com.anamariafelix.ms_event_manager.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode @ToString
public class EventCreateDTO {

    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
}
