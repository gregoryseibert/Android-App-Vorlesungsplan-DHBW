package de.gregoryseibert.vorlesungsplandhbw.data_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        if(lectureList.size() == 0) {
            lectureList.add(new Lecture());
        }

        return lectureList;
    }

    public void sortLectureList() {
        Collections.sort(lectureList, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture lec1, Lecture lec2) {
                return lec1.getStartTime() < lec2.getStartTime() ? -1 : (lec1.getStartTime() > lec2.getStartTime()) ? 1 : 0;
            }
        });
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
