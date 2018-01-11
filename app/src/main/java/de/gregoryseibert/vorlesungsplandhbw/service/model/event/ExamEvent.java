package de.gregoryseibert.vorlesungsplandhbw.service.model.event;

import android.support.annotation.NonNull;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class ExamEvent extends Event {
    public ExamEvent(SimpleDate startDate, SimpleDate endDate, @NonNull String title, String room) {
        super(startDate, endDate, title, room, null);
    }

    @Override
    public String toString() {
        return "Klausur '" + title + "': " + startDate.getFormatTime() + " - " + endDate.getFormatTime();
    }
}
