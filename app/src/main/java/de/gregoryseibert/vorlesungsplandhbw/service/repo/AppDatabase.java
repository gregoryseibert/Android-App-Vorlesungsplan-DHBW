package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

@Database(entities = {Event.class}, version = 1)
@TypeConverters({SimpleDateTypeConverter.class, EventTypeTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDAO eventDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "event-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
