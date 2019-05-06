package com.android.loanassistant.model;

import java.io.Serializable;

public class Appointed implements Serializable {
    public String name;
    public String phone;
    public String amount;

    public Appointed() {
    }

    public Appointed(String name, String phone, String amount) {
        this.name = name;
        this.phone = phone;
        this.amount = amount;
    }

    public Appointed( String phone, String amount) {
        this.phone = phone;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Appointed{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
