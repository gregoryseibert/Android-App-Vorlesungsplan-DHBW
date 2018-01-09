package de.gregoryseibert.vorlesungsplandhbw.data_model;

import java.util.ArrayList;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LecturePlan {
    private ArrayList<Lecture> lectureList;

    public LecturePlan() {
        lectureList = new ArrayList<>();
    }

    public void addLecture(long startTime, long endTime, String title, String lecturer, String room, boolean lIsExam) {
        lectureList.add(new Lecture(startTime, endTime, title, lecturer, room, lIsExam));
    }

    public ArrayList<Lecture> getLectureList() {
        return lectureList;
    }

    @Override
    public String toString() {
        String str = "";

        for(Lecture lecture : lectureList) {
            str += lecture.toString();
        }

        return str;
    }
}
