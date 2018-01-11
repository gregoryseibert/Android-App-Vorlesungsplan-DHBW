package de.gregoryseibert.vorlesungsplandhbw.service.model.event;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class PauseEvent extends Event {
    public PauseEvent(SimpleDate startDate, SimpleDate endDate) {
        super(startDate, endDate, "PauseEvent", null, null);
    }

    @Override
    public String toString() {
        return "Pause: " + startDate.getFormatTime() + " - " + endDate.getFormatTime();
    }
}
