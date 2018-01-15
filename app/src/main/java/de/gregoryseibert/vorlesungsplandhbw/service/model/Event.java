package de.gregoryseibert.vorlesungsplandhbw.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

@Entity
public class Event implements Comparable<Event> {
    @PrimaryKey
    @ColumnInfo(name = "startdate")
    public SimpleDate startDate;
    @ColumnInfo(name = "enddate")
    public SimpleDate endDate;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "room")
    public String room;
    @ColumnInfo(name = "lecturer")
    public String lecturer;
    @ColumnInfo(name = "type")
    public EventType type;

    public Event(SimpleDate startDate, SimpleDate endDate, String title, String room, String lecturer, EventType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.room = room;
        this.lecturer = lecturer;
        this.type = type;
    }

    @Override
    public String toString() {
        if(type == EventType.EMPTY) {
            return title;
        }

        return type.toString() + " '" + title + "': " + startDate.getFormatTime() + " - " + endDate.getFormatTime() + ", " + startDate.getFormatDate();
    }

    @Override
    public int compareTo(@NonNull Event event) {
        SimpleDate date = event.startDate;

        if(this.startDate != null && date != null) {
            return startDate.getMillis() < date.getMillis() ? -1 : (startDate.getMillis() > date.getMillis()) ? 1 : 0;
        }

        return 0;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public SimpleDate getStartDate() {
        return startDate;
    }

    public void setStartDate(SimpleDate startDate) {
        this.startDate = startDate;
    }

    public SimpleDate getEndDate() {
        return endDate;
    }

    public void setEndDate(SimpleDate endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }
}
