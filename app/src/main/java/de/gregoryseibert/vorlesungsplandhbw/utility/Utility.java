package de.gregoryseibert.vorlesungsplandhbw.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static Date getDate(int day, int month, int year, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'Uhr, am' dd.MM.yyyy");
        return sdf.format(date);
    }

    public static String formatDateSimple(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(date);
    }
}
