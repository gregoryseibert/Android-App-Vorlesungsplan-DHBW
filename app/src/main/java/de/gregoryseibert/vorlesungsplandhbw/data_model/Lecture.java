package de.gregoryseibert.vorlesungsplandhbw.data_model;

import java.text.DateFormat;
import java.util.Date;

import de.gregoryseibert.vorlesungsplandhbw.utility.Utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class Lecture {
    private long startTime, endTime;
    private String title, lecturer, room;
    private boolean isExam;

    public Lecture(long startTime, long endTime, String title, String lecturer, String room, boolean isExam) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.lecturer = lecturer;
        this.room = room;
        this.isExam = isExam;
    }

    public long getStartTime() {
        return startTime;
    }

    public Date getStartTimeDate() {
        Date date = new Date();
        date.setTime(startTime);
        return date;
    }

    public long getEndTime() {
        return endTime;
    }

    public Date getEndTimeDate() {
        Date date = new Date();
        date.setTime(endTime);
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getRoom() {
        return room;
    }

    public boolean isExam() {
        return isExam;
    }

    @Override
    public String toString() {
        String str = "Lecture:\n";
        str += "Start: " + Utility.formatDate(this.getStartTimeDate()) + "\n";
        str += "End: " + Utility.formatDate(this.getEndTimeDate()) + "\n";
        str += "Title: " + title + "\n";
        if(!isExam) {
            str += "Lecturer: " + lecturer + "\n";
        }
        str += "Room: " + room + "\n";
        str += "\n";

        return str;
    }
}
