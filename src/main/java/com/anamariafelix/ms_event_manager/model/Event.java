package com.anamariafelix.ms_event_manager.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "events")
@CompoundIndexes({ @CompoundIndex(name = "unique_event_name_date", def = "{'eventName' : 1, 'dateTime': 1}", unique = true)})
public class Event {

    @Id
    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String zipCode;

    private String street;
    private String neighborhood;
    private String city;
    private String uf;
}