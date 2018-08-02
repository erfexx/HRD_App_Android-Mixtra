package com.example.kayangan.absencehrd.Model;


/**
 * Created by KAYANGAN on 2/27/2018.
 */

public class User {
    private String Name;
    private String Password;
    private String Zone;
    private String created_at;
    private String updated_at;

    public User() {
    }

    public User(String name, String password, String zone, String created_at, String updated_at) {
        Name = name;
        Password = password;
        Zone = zone;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }
}
