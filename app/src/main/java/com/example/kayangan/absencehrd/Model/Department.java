package com.example.kayangan.absencehrd.Model;

/**
 * Created by Kevin E on 6/4/2018.
 */

public class Department {
    int id;
    String dept;

    public Department() {

    }

    public Department(int id, String dept){
        this.id = id;
        this.dept = dept;
    }

    public Department(String dept){
        this.dept = dept;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}

