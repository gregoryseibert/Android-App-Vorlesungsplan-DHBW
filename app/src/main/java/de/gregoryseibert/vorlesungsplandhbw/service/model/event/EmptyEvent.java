package de.gregoryseibert.vorlesungsplandhbw.service.model.event;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EmptyEvent extends Event {
    public EmptyEvent() {
        super(null, null, "Es konnten keine Vorlesungen gefunden werden.", null, null);
    }

    @Override
    public String toString() {
        return "Keine Vorlesungen.";
    }
}
