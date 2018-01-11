package de.gregoryseibert.vorlesungsplandhbw.service.model.event;

import android.support.annotation.NonNull;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LectureEvent extends Event {
    public LectureEvent(SimpleDate startDate, SimpleDate endDate, @NonNull String title, String room, String lecturer) {
        super(startDate, endDate, title, room, lecturer);
    }

    @Override
    public String toString() {
        return "Vorlesung '" + title + "': " + startDate.getFormatTime() + " - " + endDate.getFormatTime();
    }
}
