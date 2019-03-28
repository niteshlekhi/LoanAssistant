package com.android.loanassistant.model;

public class Collector {
    private String name;
    private String email;
    private String phone;
    private String aadhar;

    public Collector() {
    }

    public Collector(String name, String email, String phone, String aadhar) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.aadhar = aadhar;
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
}
