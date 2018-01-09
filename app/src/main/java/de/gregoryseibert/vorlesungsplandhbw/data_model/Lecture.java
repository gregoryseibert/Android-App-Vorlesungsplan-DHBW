package de.gregoryseibert.vorlesungsplandhbw.data_model;

import java.util.Date;
import de.gregoryseibert.vorlesungsplandhbw.utility.Utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class Lecture {
    private long startTime, endTime;
    private String title, lecturer, room;
    private LectureType type;

    public Lecture() {
        this.title = "Es konnten keine Vorlesungen gefunden werden.";
        this.type = LectureType.EMPTY;
    }

    public Lecture(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = "Pause";
        this.type = LectureType.PAUSE;
    }

    public Lecture(long startTime, long endTime, String title, String lecturer, String room, boolean isExam) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.lecturer = lecturer;
        this.room = room;
        if (isExam) {
            this.type = LectureType.EXAM;
        } else {
            this.type = LectureType.LECTURE;
        }
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

    public LectureType getType() {
        return type;
    }

    public String getCombinedDate() {
        //return Utility.formatDateTime(new Date(startTime)) + " bis " + Utility.formatDateTime(new Date(endTime)) + " Uhr, am " + Utility.formatDateSimple(new Date(startTime));
        return Utility.formatDateTime(new Date(startTime)) + " bis " + Utility.formatDateTime(new Date(endTime)) + " Uhr";
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
}
