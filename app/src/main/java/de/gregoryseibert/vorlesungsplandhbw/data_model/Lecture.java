package de.gregoryseibert.vorlesungsplandhbw.data_model;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Date;
import de.gregoryseibert.vorlesungsplandhbw.utility.Utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class Lecture implements Comparable<Lecture> {
    private SimpleDate startDate, endDate;
    private String title, lecturer, room;
    private LectureType type;

    public Lecture() {
        this.title = "Es konnten keine Vorlesungen gefunden werden.";
        this.type = LectureType.EMPTY;
    }

    public Lecture(SimpleDate startDate, SimpleDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = "Pause";
        this.type = LectureType.PAUSE;
    }

    public Lecture(SimpleDate startDate, SimpleDate endDate, String title, String lecturer, String room, boolean isExam) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.lecturer = lecturer;
        this.room = room;
        if (isExam) {
            this.type = LectureType.EXAM;
        } else {
            this.type = LectureType.LECTURE;
        }
    }

    public LectureType getType() {
        return type;
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

    public SimpleDate getStartDate() {
        return startDate;
    }

    public SimpleDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Lecture '" + title + "': " + startDate.getFormatDate();
    }

    @Override
    public int compareTo(@NonNull Lecture lecture) {
        SimpleDate date = lecture.getStartDate();
        return startDate.getMillis() < date.getMillis() ? -1 : (startDate.getMillis() > date.getMillis()) ? 1 : 0;
    }
}
