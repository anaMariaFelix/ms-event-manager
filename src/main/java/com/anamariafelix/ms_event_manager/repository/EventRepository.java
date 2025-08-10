package com.anamariafelix.ms_event_manager.repository;

import com.anamariafelix.ms_event_manager.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findAllByDeletedFalseOrderByEventNameAsc();

    Event findByEventNameAndDateTimeAndDeletedFalse(String eventName, LocalDateTime dateTime);

    Optional<Event> findByIdAndDeletedFalse(String id);
}
