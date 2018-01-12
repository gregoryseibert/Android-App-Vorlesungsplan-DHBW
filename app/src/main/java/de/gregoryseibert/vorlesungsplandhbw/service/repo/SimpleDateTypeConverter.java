package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import android.arch.persistence.room.TypeConverter;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;

/**
 * Created by Gregory Seibert on 12.01.2018.
 */

public class SimpleDateTypeConverter {
    @TypeConverter
    public SimpleDate toDate(Long value) {
        return value == null ? null : new SimpleDate(value);
    }

    @TypeConverter
    public Long toLong(SimpleDate date) {
        return date == null ? null : date.getMillis();
    }
}
