package com.example.kayangan.absencehrd.Model;


/**
 * Created by KAYANGAN on 2/27/2018.
 */

public class User {
    private String Name;
    private String Password;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        /*Clock_In = clock_In;
        Clock_Out = clock_Out;
        Date = date;
        this.isTap = isTap;*/
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    /*public String getClock_In() {
        return Clock_In;
    }

    public void setClock_In(String clock_In) {
        Clock_In = clock_In;
    }

    public String getClock_Out() {
        return Clock_Out;
    }

    public void setClock_Out(String clock_Out) {
        Clock_Out = clock_Out;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIsTap() {
        return isTap;
    }

    public void setIsTap(String isTap) {
        this.isTap = isTap;
    }*/

}
