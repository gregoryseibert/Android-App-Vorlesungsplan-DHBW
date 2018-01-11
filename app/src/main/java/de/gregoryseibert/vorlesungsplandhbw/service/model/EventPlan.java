package de.gregoryseibert.vorlesungsplandhbw.service.model;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.service.model.event.EmptyEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.Event;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class EventPlan {
    private ArrayList<EventDay> eventDays;

    public EventPlan(SimpleDate firstDate) {
        eventDays = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            SimpleDate date = new SimpleDate(firstDate);
            date.addDays(i);
            eventDays.add(new EventDay(date));
        }
    }

    public void addEvent(int index, Event event) {
        eventDays.get(index).addEvent(event);
    }

    public void addEmptyEvents() {
        for(EventDay eventDay : eventDays) {
            if(eventDay.eventList.size() == 0) {
                eventDay.addEvent(new EmptyEvent());
            }
        }
    }

    public EventDay getEventDay(SimpleDate date) {
        for(EventDay eventDay : eventDays) {
            if(eventDay.day.isSameDay(date)) {
                return eventDay;
            }
        }

        return null;
    }

    public void sortEventList() {
        for(EventDay eventDay : eventDays) {
            eventDay.sortEventList();
        }
    }

    @Override
    public String toString() {
        String str = "";
        for (EventDay eventDay : eventDays) {
            str += eventDay.toString() + "\n";
        }
        return str;
    }
}
