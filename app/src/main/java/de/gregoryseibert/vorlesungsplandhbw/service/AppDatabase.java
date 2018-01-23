package de.gregoryseibert.vorlesungsplandhbw.service;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import de.gregoryseibert.vorlesungsplandhbw.model.Event;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

@Database(entities = {Event.class}, version = 2)
@TypeConverters({SimpleDateTypeConverter.class, EventTypeTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDAO eventDao();
}
