package com.android.loanassistant.model;

public class Login {
    private String email;
    private String password;
    private int type;

    public Login() {
    }

    public Login(String email, String password, int type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public Login(String password, int type) {
        this.password = password;
        this.type = type;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
