package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

@Dao
public interface EventDAO {
    @Query("SELECT * FROM event WHERE startDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> getAllByRange(long rangeStart, long rangeEnd);

    @Query("DELETE FROM event")
    void deleteAll();

    @Insert
    void insertAll(List<Event> event);
}
