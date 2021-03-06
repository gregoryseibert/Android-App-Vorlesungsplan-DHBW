package de.gregoryseibert.vorlesungsplandhbw.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Gregory Seibert on 10.01.2018.
 */

public class SimpleDate {
    private Calendar c;
    private static SimpleDateFormat sdfDateTime, sdfDate, sdfDateShort, sdfTime;

    public SimpleDate(int day, int month, int year, int hour, int minute) {
        c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        initSimpleDateFormat();
    }

    public SimpleDate(SimpleDate date) {
        c = Calendar.getInstance();
        c.setTimeInMillis(date.getMillis());
        initSimpleDateFormat();

    }

    public SimpleDate(long millis) {
        c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        initSimpleDateFormat();
    }

    public SimpleDate() {
        c = Calendar.getInstance();
        initSimpleDateFormat();
    }

    private void initSimpleDateFormat() {
        sdfDateTime = new SimpleDateFormat("HH:mm 'Uhr, am' dd.MM.yyyy", Locale.GERMAN);
        sdfDate = new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.GERMAN);
        sdfDateShort = new SimpleDateFormat("EEEE", Locale.GERMAN);
        sdfTime = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    }

    public SimpleDate getFirstDayOfWeek() {
        SimpleDate firstDay = new SimpleDate(this);
        firstDay.getCalendar().add(Calendar.DAY_OF_MONTH, - firstDay.getDayOfWeek());

        return firstDay;
    }

    public SimpleDate getLastDayOfWeek() {
        SimpleDate lastDay = new SimpleDate(this);
        lastDay.getCalendar().add(Calendar.DAY_OF_MONTH, 6 - lastDay.getDayOfWeek());

        return lastDay;
    }

    public int getDayOfWeek() {
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }

    public void addDays(int amount) {
        c.add(Calendar.DAY_OF_MONTH, amount);
    }

    public void addHours(int amount) {
        c.add(Calendar.HOUR, amount);
    }

    public void setDay(int day) {
        c.set(Calendar.DAY_OF_MONTH, day);
    }

    public int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getWeek() { return c.get(Calendar.WEEK_OF_YEAR); }

    public int getMonth() {
        return c.get(Calendar.MONTH);
    }

    public int getYear() {
        return c.get(Calendar.YEAR);
    }

    public long getMillis(){
        return c.getTimeInMillis();
    }

    public String getFormatDateTime() {
        return sdfDateTime.format(c.getTime());
    }

    public String getFormatDate() {
        return sdfDate.format(c.getTime());
    }

    public String getFormatDateShort() {
        return sdfDateShort.format(c.getTime());
    }

    public String getFormatTime() {
        return sdfTime.format(c.getTime());
    }

    public Calendar getCalendar() { return c; }
}
