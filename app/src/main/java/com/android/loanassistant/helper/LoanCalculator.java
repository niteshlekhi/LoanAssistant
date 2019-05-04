package com.android.loanassistant.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoanCalculator {

    private static Date d;
    private float loanAmount;
    private int time;
    private float interestRate;
    private float interest;
    public static Date date;
    public static SimpleDateFormat dateFormat;
    private static int yr, month, day;

    public LoanCalculator() {
    }

    public LoanCalculator(float loanAmount, float interestRate, int time
    ) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.time = time;
    }

 /*   public LoanCalculator(float loanAmount, float interestRate, int time, char duration) {
        this.loanAmount = loanAmount;
        this.time = time;
        this.interestRate = interestRate;
        this.duration = duration;
    }*/

    public double getLoanAmount() {
        return loanAmount;
    }


    public float calculateInterest(float loanAmount, float interestRate, int time) {
        interest = (loanAmount * interestRate * time) / 1200;
        return interest;
    }

    public static Date getDateFromString(String s) {
        d = null;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            d = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        yr = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        ++month;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String s = String.valueOf(day + "/" + month + "/" + yr);

        s = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        return s;


    }

    public static String getDueDate(String initDate, int time) {

        String date = "";
        Date initialDate = null;

        try {
            initialDate = new SimpleDateFormat("dd/MM/yyyy").parse(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(initialDate);

        cal.add(Calendar.MONTH, time);

//        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
        return date;
    }

    public static List<String> installmentDates(String initialDate, String dueDate, char type, int time) {
        List<String> list = new ArrayList<>();
        d = null;
        try {
            d = new SimpleDateFormat("dd/MM/yyyy").parse(initialDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (type == 'm') {
            for (int i = 1; i <= time; i++) {
                list.add(getDueDate(initialDate, i));
            }

        } else if (type == 's') {
            list.add(dueDate);
        }

        return list;
    }

    public static long getDifferenceDays(String s1, String s2) {

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = new SimpleDateFormat("dd/MM/yyyy").parse(s1);
            d2 = new SimpleDateFormat("dd/MM/yyyy").parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

/*    public double getMonthlyPayment() {
        double monthlyPayment;
        double monthlyInterestRate;
        int numberOfPayments;
        if (time != 0 && interestRate != 0) {
            //calculate the monthly payment
            monthlyInterestRate = interestRate / 1200;
            numberOfPayments = time * 12;

            monthlyPayment =
                    (loanAmount * monthlyInterestRate) /
                            (1 - (1 / Math.pow((1 + monthlyInterestRate), numberOfPayments)));

            monthlyPayment = Math.round(monthlyPayment * 100) / 100.0;
        } else
            monthlyPayment = 0;
        return monthlyPayment;
    }*/


    public int getTime() {
        return time;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setLoanAmount(float loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public String toString() {
        return getLoanAmount() + "," + getTime() + "," +
                getInterestRate();
    }
}
