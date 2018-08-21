package com.example.kayangan.absencehrd.Model;


/**
 * Created by KAYANGAN on 2/27/2018.
 */

public class User {
    private String Name;
    private String Password;
    private String Gender;
    private String timestamp;
    private String modified_date;
    private int id;

    public User() {
    }

    public User(String name, String password, String gender, String timestamp, String modified_date, int id) {
        Name = name;
        Password = password;
        Gender = gender;
        this.timestamp = timestamp;
        this.modified_date = modified_date;
        this.id = id;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
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
}
