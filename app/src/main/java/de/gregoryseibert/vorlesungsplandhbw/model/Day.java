package de.gregoryseibert.vorlesungsplandhbw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregory Seibert on 19.01.2018.
 */

public class Day {
    private List<Event> events;
    private String title;

    public Day(String title) {
        this.title = title;
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getEventsSize() { return events.size(); }

    public String getTitle() {
        return title;
    }
}
