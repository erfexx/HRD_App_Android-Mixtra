package com.mit.mobile.absencehrd.Model;

/**
 * Created by Kevin E on 6/4/2018.
 */

public class Branch {
    int id;
    String branch;

    public Branch() {
    }

    public Branch(int id, String branch) {
        this.id = id;
        this.branch = branch;
    }

    public Branch(String branch) {
        this.branch = branch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
