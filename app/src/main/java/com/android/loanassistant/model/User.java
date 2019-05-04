package com.android.loanassistant.model;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String phone;
    public String aadhar;
    public String address;

    public User() {
    }

    public User(String name, String phone, String aadhar) {
        this.name = name;
        this.phone = phone;
        this.aadhar = aadhar;
    }

    public User(String name, String phone, String aadhar, String address) {
        this.name = name;
        this.phone = phone;
        this.aadhar = aadhar;
        this.address = address;
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

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
