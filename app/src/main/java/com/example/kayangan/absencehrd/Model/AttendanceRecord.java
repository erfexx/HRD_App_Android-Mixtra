package com.example.kayangan.absencehrd.Model;


/**
 * Created by KAYANGAN on 3/1/2018.
 */

public class AttendanceRecord {

  private String date;
  private String created_at;
  private String updated_at;
  private String clock_in;
  private String clock_out;
  private String user_id;
  private String status;
  private int id;
  private String flag = "0";

  public AttendanceRecord() {
  }

  public AttendanceRecord(String date, String clock_in, String clock_out, String user_id) {
    this.date = date;
    this.clock_in = clock_in;
    this.clock_out = clock_out;
    this.user_id = user_id;
  }

  public AttendanceRecord(String date, String created_at, String updated_at, String clock_in, String clock_out, String user_id, String status) {
      this.date = date;
      this.created_at = created_at;
      this.updated_at = updated_at;
      this.clock_in = clock_in;
      this.clock_out = clock_out;
      this.user_id = user_id;
      this.status = status;

  }

    public AttendanceRecord(String date, String clock_in, String clock_out) {
    this.date = date;
    this.clock_in = clock_in;
    this.clock_out = clock_out;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
