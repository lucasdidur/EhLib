package com.ehaqui.lib.util;

import java.util.Calendar;
import java.util.Date;


public class TimeUtil
{
    private static String _daym = "d ";
    private static String _hourm = "h ";
    private static String _minutem = "m ";
    private static String _seccondm = "s ";

    private static String _day = "dia ";
    private static String _hour = "hora ";
    private static String _minute = "minuto ";
    private static String _seccond = "segundo ";

    private static String _days = "dias ";
    private static String _hours = "horas ";
    private static String _minutes = "minutos ";
    private static String _secconds = "segundos ";


    public static String formatTimeReaming(String format, long time)
    {
        if (time > 0)
        {
            long millis = time - System.currentTimeMillis();
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            while (millis >= 1000)
            {
                seconds++;
                millis -= 1000;
            }
            while (seconds >= 60)
            {
                minutes++;
                seconds -= 60;
            }
            while (minutes >= 60)
            {
                hours++;
                minutes -= 60;
            }
            while (hours >= 24)
            {
                days++;
                hours -= 24;
            }
            return timeFormat(format, days, hours, minutes, seconds);
        }

        return "Erro";
    }

    public static String formatTime(int time)
    {
        return formatTime(time, false);
    }

    public static String formatTime(int time, boolean min)
    {
        if (time > 0)
        {
            int seconds = time;
            int minutes = 0;
            int hours = 0;
            int days = 0;

            while (seconds >= 60)
            {
                minutes++;
                seconds -= 60;
            }
            while (minutes >= 60)
            {
                hours++;
                minutes -= 60;
            }
            while (hours >= 24)
            {
                days++;
                hours -= 24;
            }

            StringBuilder sb = new StringBuilder();

            if (days > 0)
                sb.append("{DAYS}" + (min ? _daym : " " + (days > 1 ? _days : _day)));

            if (hours > 0)
                sb.append("{HOURS}" + (min ? _hourm :  " " + (hours > 1 ? _hours : _hour)));

            if (minutes > 0)
                sb.append("{MINUTES}" + (min ? _minutem : " " + (minutes > 1 ? _minutes : _minute)));

            if (seconds > 0)
                sb.append("{SECONDS}" + (min ? _seccondm : " " + (seconds > 1 ? _secconds : _seccond)));

            return timeFormat(sb.toString(), days, hours, minutes, seconds);
        }

        return "Erro";
    }

    public static String timeFormat(String format, int days, int hours, int minutes, int seconds)
    {
        return format.replace("{DAYS}", "" + days).replace("{HOURS}", "" + hours).replace("{MINUTES}", "" + minutes).replace("{SECONDS}", "" + seconds);
    }

    public static Date addDate(Date date, String type, int value)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());

        switch (type)
        {
            case "seconds":
                cal.add(Calendar.SECOND, value);
                break;

            case "minutes":
                cal.add(Calendar.MINUTE, value);
                break;

            case "hours":
                cal.add(Calendar.HOUR, value);
                break;

            case "days":
                cal.add(Calendar.DATE, value);
                break;

            case "weeks":
                cal.add(Calendar.WEEK_OF_MONTH, value);
                break;

            case "months":
                cal.add(Calendar.MONTH, value);
                break;

            case "years":
                cal.add(Calendar.YEAR, value);
                break;
        }

        return cal.getTime();
    }

    public static double round(double value, int places)
    {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static long getDiff(Long next)
    {
        return next - System.currentTimeMillis();
    }

    public static boolean isFuture(long time)
    {
        return (time - System.currentTimeMillis()) > 0;
    }

    public static boolean isPass(long time)
    {
        return (time - System.currentTimeMillis()) < 0;
    }

}
