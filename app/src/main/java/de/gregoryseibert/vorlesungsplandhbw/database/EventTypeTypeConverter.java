package de.gregoryseibert.vorlesungsplandhbw.database;

import android.arch.persistence.room.TypeConverter;

import de.gregoryseibert.vorlesungsplandhbw.model.Event.EventType;

/**
 * Created by Gregory Seibert on 12.01.2018.
 */

public class EventTypeTypeConverter {
    @TypeConverter
    public EventType toType(String str) {
        if(str == null) {
            return null;
        } else if(str.equals("lecture")) {
            return EventType.LECTURE;
        } else if(str.equals("exam")) {
            return EventType.EXAM;
        }
        return null;
    }

    @TypeConverter
    public String toString(EventType type) {
        if(type == null) {
            return null;
        } else if(type == EventType.LECTURE) {
            return "lecture";
        } else if(type == EventType.EXAM) {
            return "exam";
        }
        return null;
    }
}
