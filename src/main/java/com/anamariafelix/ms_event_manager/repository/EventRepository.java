package com.anamariafelix.ms_event_manager.repository;

import com.anamariafelix.ms_event_manager.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findAllByOrderByEventNameAsc();
}
