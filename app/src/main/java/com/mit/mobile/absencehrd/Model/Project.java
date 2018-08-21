package com.mit.mobile.absencehrd.Model;

/**
 * Created by Kevin E on 6/4/2018.
 */

public class Project {
    int id;
    String project;

    public Project() {
    }

    public Project(int id, String project) {
        this.id = id;
        this.project = project;
    }

    public Project(String project) {
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
