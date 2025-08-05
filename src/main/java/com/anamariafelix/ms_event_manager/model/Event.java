package com.anamariafelix.ms_event_manager.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "events")
public class Event {

    @Id
    private String id;

    private String eventName;
    private LocalDateTime dateTime;
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}