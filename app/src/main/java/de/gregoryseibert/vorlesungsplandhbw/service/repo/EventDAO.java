package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.model.Event;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

@Dao
public interface EventDAO {
    @Query("SELECT * FROM event WHERE startDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> getAllByRange(long rangeStart, long rangeEnd);

    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("DELETE FROM event WHERE startDate BETWEEN :rangeStart AND :rangeEnd")
    void deleteAllByRange(long rangeStart, long rangeEnd);

    @Query("DELETE FROM event")
    void deleteAll();

    @Insert
    void insertAll(List<Event> event);
}

