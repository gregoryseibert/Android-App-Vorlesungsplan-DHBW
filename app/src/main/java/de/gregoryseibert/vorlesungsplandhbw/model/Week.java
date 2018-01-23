package de.gregoryseibert.vorlesungsplandhbw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gregory Seibert on 19.01.2018.
 */

public class Week {
    private List<Day> days;
    private SimpleDate firstDate;

    public Week(SimpleDate firstDate) {
        this.days = new ArrayList<>();
        this.firstDate = new SimpleDate(firstDate);

        SimpleDate date = new SimpleDate(firstDate);
        for(int i = 0; i < 6; i++) {
            days.add(new Day(date.getFormatDateShort()));
            date.addDays(1);
        }
    }

    public void insertEvent(Event event) {
        int index = event.getStartDate().getDayOfWeek();
        if(index < 6) {
            days.get(index).addEvent(event);
        }
    }

    public void insertEvents(List<Event> events) {
        for(Event event : events) {
            insertEvent(event);
        }
    }

    public List<Event> getEventsOfDay(int day) {
        if(day >= 0 && day < 6) {
            return days.get(day).getEvents();
        }

        return null;
    }

    public List<Day> getDays() {
        return days;
    }

    public SimpleDate getFirstDate() {
        return firstDate;
    }
}
