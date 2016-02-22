package com.yumnote.yumnote.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jen on 2/21/16.
 */
public class PlanDate {
    private Calendar calendar;

    public PlanDate() {
        this(Calendar.getInstance());
    }

    PlanDate(long timeInMillis) {
        this(getCalendarFromMillis(timeInMillis));
    }

    private PlanDate(Calendar otherCalendar) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.YEAR, otherCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, otherCalendar.get(Calendar.MONTH));
        calendar.set(Calendar.DATE, otherCalendar.get(Calendar.DATE));
    }

    private static Calendar getCalendarFromMillis(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        return c;
    }

    public PlanDate addDays(int numDays) {
        calendar.add(Calendar.DATE, numDays);
        return this;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDate() {
        return calendar.get(Calendar.DATE);
    }

    public long getMillis() {
        return calendar.getTimeInMillis();
    }
}
