package com.android.loanassistant.helper;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeHelper {

    public static Date date;
    public static SimpleDateFormat dateFormat;
    private static int yr, month, day;


    public static String currentdate(Context context) {
        Calendar calendar = Calendar.getInstance();
        yr = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        ++month;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String s = String.valueOf(day + "/" + month + "/" + yr);
        return s;
    }

    public static int getHour() {
        date = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("hh", Locale.getDefault());
        int hrs = Integer.parseInt(dateFormat.format(date));
        return hrs;
    }

    public static String getAmpm() {
        dateFormat = new SimpleDateFormat("a", Locale.getDefault());
        String ampm = dateFormat.format(date);
        return ampm;
    }

    public static boolean dateAfterCurrent(String s) {
//        String s2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
//        Date strCurrent = null;

        try {
            strDate = df1.parse(s);
//            strCurrent = df1.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*Calendar cal1 = Calendar.getInstance();
        cal1.setTime(strDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(strCurrent);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");*/
        if (strDate.after(new Date()))
            return true;
        else
            return false;


    }

    public static boolean toBeforeFromDate(String s1, String s2) {
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFrom = null;
        Date dateTo = null;

        try {
            dateFrom = df1.parse(s1);
            dateTo = df1.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dateFrom);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateTo);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (cal1.after(cal2))
            return true;
        else
            return false;
    }


    public static List<String> getDates(String s1, String s2) {
        ArrayList<String> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date dateFrom = null;
        Date dateTo = null;

        try {
            dateFrom = df1.parse(s1);
            dateTo = df1.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dateFrom);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateTo);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while (!cal1.after(cal2)) {
            dates.add(dateFormat.format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
}