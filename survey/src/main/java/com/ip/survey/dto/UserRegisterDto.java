package com.ip.survey.dto;

import com.sun.istack.NotNull;


public class UserRegisterDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String password2;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
