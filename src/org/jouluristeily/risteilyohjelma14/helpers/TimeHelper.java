package org.jouluristeily.risteilyohjelma14.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelper {

    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String utcPlus2Time = df.format(new Date());
        return utcPlus2Time;
    }

    public static int getCurrentHours() {
        return Integer.parseInt(getCurrentTime().substring(0, 2));
    }

    public static int getCurrentMinutes() {
        return Integer.parseInt(getCurrentTime().substring(3, 5));
    }

    public static long convertTimeToMs(String hhMm) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date;
        long timestamp;
        String toPvm = "2013-11-28";
        String pePvm = "2013-11-29";
        String fullString;
        try {
            // if 20:00-24:00 -> 28.11
            if (hhMm.compareTo("20:00") >= 0) {
                fullString = toPvm + " " + hhMm;
            }
            // if 00:00-19:59 --> 29.11
            else {
                fullString = pePvm + " " + hhMm;
            }
            date = format.parse(fullString);
            timestamp = date.getTime();
        } catch (ParseException e) {
            timestamp = 0;
        }
        return timestamp;
    }

    public static long getTimeInMs() {
        return System.currentTimeMillis();
    }
}
