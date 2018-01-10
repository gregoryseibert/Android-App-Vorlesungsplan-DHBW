package de.gregoryseibert.vorlesungsplandhbw.data_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import de.gregoryseibert.vorlesungsplandhbw.utility.Utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LecturePlan {
    private ArrayList<Lecture> lectureList;

    public LecturePlan() {
        lectureList = new ArrayList<>();
    }

    public void addLecture(SimpleDate startDate, SimpleDate endDate, String title, String lecturer, String room, boolean lIsExam) {
        lectureList.add(new Lecture(startDate, endDate, title, lecturer, room, lIsExam));
    }

    public ArrayList<Lecture> getLectureList() {
        if(lectureList.size() == 0) {
            lectureList.add(new Lecture());
        }

        return lectureList;
    }

    public ArrayList<Lecture> getLectureListOfDate(SimpleDate date) {
        ArrayList<Lecture> lectureListDay = new ArrayList<>();

        if(lectureList.size() > 0) {
            for(Lecture lecture : lectureList) {
                if(lecture.getStartDate().getDay() == date.getDay()) {
                    lectureListDay.add(lecture);
                }
            }

            if(lectureListDay.size() == 0) {
                lectureListDay.add(new Lecture());
            }
        } else {
            lectureListDay.add(new Lecture());
        }

        return lectureListDay;
    }

    public void sortLectureList() {
        Collections.sort(lectureList);
    }

    @Override
    public String toString() {
        String str = "";

        for(Lecture lecture : lectureList) {
            str += lecture.toString() + "\n";
        }

        return str;
    }
}
