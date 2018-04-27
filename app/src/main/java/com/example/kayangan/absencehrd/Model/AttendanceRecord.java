package com.example.kayangan.absencehrd.Model;

import java.time.Clock;
import java.util.Date;

/**
 * Created by KAYANGAN on 3/1/2018.
 */

public class AttendanceRecord {

  private String date;
  private String clock_in;
  private String clock_out;
  private String user_id;
  private String flag = "0";

  public AttendanceRecord() {
  }


  public AttendanceRecord(String date, String clock_in, String clock_out, String user_id, String flag) {
    this.date = date;
    this.clock_in = clock_in;
    this.clock_out = clock_out;
    this.user_id = user_id;
    this.flag = flag;
  }

  public AttendanceRecord(String date, String clock_in, String clock_out) {
    this.date = date;
    this.clock_in = clock_in;
    this.clock_out = clock_out;
  }

  public AttendanceRecord(String clock_out, String flag) {
    this.clock_out = clock_out;
    this.flag = flag;
  }

  public AttendanceRecord(String flag) {
    this.flag = flag;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getClock_in() {
    return clock_in;
  }

  public void setClock_in(String clock_in) {
    this.clock_in = clock_in;
  }

  public String getClock_out() {
    return clock_out;
  }

  public void setClock_out(String clock_out) {
    this.clock_out = clock_out;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }
}
