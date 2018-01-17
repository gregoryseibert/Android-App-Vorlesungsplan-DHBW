package de.gregoryseibert.vorlesungsplandhbw.model;

import java.util.ArrayList;

/**
 * Created by Gregory Seibert on 17.01.2018.
 */

public class EventHolder {
    private ArrayList<Event> events;

    public EventHolder() {
        events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeAllEvents() {
        events.clear();
    }

    public ArrayList<Event> getAllEvents() {
        return events;
    }
}
