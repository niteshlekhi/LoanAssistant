package com.android.loanassistant.model;

import java.io.Serializable;

public class Collector implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String aadhar;
    private String address;
    private String dpUrl;

    public Collector() {
    }

    public Collector(String name, String email, String phone, String aadhar) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.aadhar = aadhar;
    }

    public Collector(String name, String email, String phone, String aadhar, String address, String dpUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.aadhar = aadhar;
        this.address = address;
        this.dpUrl = dpUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }
}
