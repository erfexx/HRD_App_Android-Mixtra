package com.example.kayangan.absencehrd.Model;

/**
 * Created by Kevin E on 3/26/2018.
 */

public class Task {
    int _id;
    String tname;
    String tdesc;
    String tduedate;
    int tassign;

    // Empty constructor
    public Task(){

    }
    // constructor
    public Task(int id, String tname, String tdesc, String tduedate, int tassign){
        this._id = id;
        this.tname = tname;
        this.tdesc = tdesc;
        this.tduedate = tduedate;
        this.tassign = tassign;
    }

    // constructor
    public Task(String tname, String tdesc, String tduedate, int tassign){
        this.tname = tname;
        this.tdesc = tdesc;
        this.tduedate = tduedate;
        this.tassign = tassign;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
    }

    public String getTduedate() {
        return tduedate;
    }

    public void setTduedate(String tduedate) {
        this.tduedate = tduedate;
    }

    public int getTassign() {
        return tassign;
    }

    public void setTassign(int tassign) {
        this.tassign = tassign;
    }
}

