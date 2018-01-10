package de.gregoryseibert.vorlesungsplandhbw.data_model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Gregory Seibert on 10.01.2018.
 */

public class SimpleDate {
    private Calendar c;

    public SimpleDate(int day, int month, int year, int hour, int minute) {
        c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
    }

    public SimpleDate(long millis) {
        c = Calendar.getInstance();
        c.setTimeInMillis(millis);
    }

    public SimpleDate() {
        c = Calendar.getInstance();

    }

    public boolean isSameWeek(SimpleDate date) {
        int year1 = c.get(Calendar.YEAR);
        int week1 = c.get(Calendar.WEEK_OF_YEAR);

        Calendar c2 = date.getCalendar();
        int year2 = c2.get(Calendar.YEAR);
        int week2 = c2.get(Calendar.WEEK_OF_YEAR);

        return year1 == year2 && week1 == week2;
    }

    public void addDays(int amount) {
        c.add(Calendar.DAY_OF_MONTH, amount);
    }

    public int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return c.get(Calendar.MONTH);
    }

    public int getYear() {
        return c.get(Calendar.YEAR);
    }

    public long getMillis(){
        return c.getTimeInMillis();
    }

    public Calendar getCalendar() {
        return c;
    }

    public String getFormatDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'Uhr, am' dd.MM.yyyy", Locale.GERMAN);
        return sdf.format(c.getTime());
    }

    public String getFormatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.GERMAN);
        return sdf.format(c.getTime());
    }

    public String getFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        return sdf.format(c.getTime());
    }

    public void copy(SimpleDate date) {
        c.setTimeInMillis(date.getMillis());
    }
}
