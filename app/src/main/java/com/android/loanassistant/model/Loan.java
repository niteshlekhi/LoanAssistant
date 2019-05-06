package com.android.loanassistant.model;

import java.io.Serializable;
import java.util.List;

public class Loan implements Serializable {
    public String phone;
    public String amount;
    public String rate;
    public int time;
    public String startDate;
    public String dueDate;
    public String interest;
    public List<String> dates;
    public String appoint;
    public String paid;

    public Loan() {
    }

    public Loan(String appoint) {
        this.appoint = appoint;
    }

    public Loan(String phone, String amount, String rate, int time, String startDate, String dueDate, String interest, List<String> dates) {
        this.phone = phone;
        this.amount = amount;
        this.rate = rate;
        this.time = time;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.interest = interest;
        this.dates = dates;
    }

    public Loan(String amount, String rate, int time, String interest, String startDate, String dueDate, String phone, List<String> dates) {
        this.amount = amount;
        this.rate = rate;
        this.time = time;
        this.interest = interest;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.phone = phone;
        this.dates = dates;
    }

    public Loan(String amount, String rate, int time, String interest, String startDate, String dueDate, String phone, List<String> dates, String appoint, String paid) {
        this.amount = amount;
        this.rate = rate;
        this.time = time;
        this.interest = interest;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.phone = phone;
        this.dates = dates;
        this.appoint = appoint;
        this.paid = paid;
    }

    public Loan(String amount, String rate, int time) {
        this.amount = amount;
        this.rate = rate;
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public List<String> getDates() {
        return dates;
    }

//    public int getDatesCount(){
//        return dates.size();
//    }


    public String getAppoint() {
        return appoint;
    }

    public void setAppoint(String appoint) {
        this.appoint = appoint;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "phone='" + phone + '\'' +
                ", amount='" + amount + '\'' +
                ", rate='" + rate + '\'' +
                ", time=" + time +
                ", startDate='" + startDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", interest='" + interest + '\'' +
                ", dates=" + dates +
                '}';
    }
}
