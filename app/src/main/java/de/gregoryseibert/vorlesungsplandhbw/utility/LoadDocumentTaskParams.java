package de.gregoryseibert.vorlesungsplandhbw.utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LoadDocumentTaskParams {
    String base;
    String key;
    int day;
    int month;
    int year;

    public LoadDocumentTaskParams(String base, String key, int day, int month, int year) {
        this.base = base;
        this.key = key;
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
