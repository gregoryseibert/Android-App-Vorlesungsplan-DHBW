package de.gregoryseibert.vorlesungsplandhbw.service.model;

import java.util.ArrayList;
import java.util.Collections;

import de.gregoryseibert.vorlesungsplandhbw.service.model.event.EmptyEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.LectureEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.PauseEvent;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventDay {
    public ArrayList<Event> eventList;
    public SimpleDate day;
    private boolean emptyDay, pausesAdded;

    public EventDay(SimpleDate date) {
        eventList = new ArrayList<>();
        this.day = date;
    }

    public boolean addEvent(Event event) {
        if(event instanceof EmptyEvent) {
            eventList.add(event);
            emptyDay = true;
        } else if(event.startDate.isSameDay(day)) {
            eventList.add(event);

            emptyDay = false;

            return true;
        }

        return false;
    }

    public void sortEventList() {
        if(! emptyDay && eventList.size() > 1) {
            Collections.sort(eventList);
            addPauses();
        }
    }

    private void addPauses() {
        if(pausesAdded) {
            for(Event event : eventList) {
                if(event instanceof PauseEvent) {
                    eventList.remove(event);
                }
            }

            pausesAdded = false;
        }

        for (int i = 0; i < eventList.size(); i++) {
            if(i % 2 == 0 && i < eventList.size() - 1) {
                eventList.add(i + 1, new PauseEvent(eventList.get(i).endDate, eventList.get(i + 1).startDate));
                pausesAdded = true;
            }
        }
    }

    @Override
    public String toString() {
        if(eventList.size() == 0) {
            return day.getFormatDate() + ": Fehler!";
        } else if(eventList.get(0) instanceof EmptyEvent) {
            return day.getFormatDate() + ": keine Events.";
        } else {
            String str = day.getFormatDate() + ":\n";
            for(Event event : eventList) {
                str += "\t" + event.toString() + "\n";
            }
            return str;
        }
    }
}
