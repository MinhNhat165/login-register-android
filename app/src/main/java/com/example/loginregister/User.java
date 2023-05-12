package com.example.loginregister;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String username;
    private String password;
    private String gender;
    private String avatar;

    public User(String email, String username, String password, String gender) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.avatar = "";
    }

    public User(String email, String username, String password, String gender, String avatar) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

