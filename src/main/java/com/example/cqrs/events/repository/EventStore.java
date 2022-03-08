package com.example.cqrs.events.repository;

import com.example.cqrs.events.events.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple in-memory store. Other good choices for general event sourcing can be kafka or cassandra. A specialized one is Apache Druid
 */
@Repository
public class EventStore {
    private Map<String, List<Event>> store = new HashMap<>();

    public void addEvent(String userId, Event event) {
        var events = store.get(userId);
        if (events == null) {
            events = new ArrayList<>();
            store.put(userId,events);
        }
        events.add(event);

    }

    public List<Event> getEvents(String userId) {
        return store.get(userId);
    }
}