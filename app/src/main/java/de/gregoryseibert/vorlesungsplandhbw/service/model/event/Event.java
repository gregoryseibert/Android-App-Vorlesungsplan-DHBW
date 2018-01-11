package de.gregoryseibert.vorlesungsplandhbw.service.model.event;

import android.support.annotation.NonNull;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public abstract class Event  implements Comparable<Event> {
    public SimpleDate startDate, endDate;
    @NonNull public String title;
    public String room;
    public String lecturer;

    public Event(SimpleDate startDate, SimpleDate endDate, @NonNull String title, String room, String lecturer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.room = room;
        this.lecturer = lecturer;
    }

    @Override
    public abstract String toString();

    @Override
    public int compareTo(@NonNull Event event) {
        SimpleDate date = event.startDate;

        if(this.startDate != null && date != null) {
            return startDate.getMillis() < date.getMillis() ? -1 : (startDate.getMillis() > date.getMillis()) ? 1 : 0;
        }

        return 0;
    }
}
