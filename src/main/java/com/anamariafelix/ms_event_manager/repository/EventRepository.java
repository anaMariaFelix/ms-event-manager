package com.anamariafelix.ms_event_manager.repository;

import com.anamariafelix.ms_event_manager.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    Event findByEventNameAndDateTimeAndDeletedFalse(String eventName, LocalDateTime dateTime);

    Optional<Event> findByIdAndDeletedFalse(String id);

    Page<Event> findAllByDeletedFalse(Pageable pageable);
}
