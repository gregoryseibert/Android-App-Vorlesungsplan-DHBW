package de.gregoryseibert.vorlesungsplandhbw.utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class Utility {
    public static String generateURL(String base, String key, int day, int month, int year) {
        String url = base + key;

        if(day != 0 && month != 0 && year != 0) {
            url += "&day=" + day;
            url += "&month=" + month;
            url += "&year=" + year;
        }

        return url;
    }
}
