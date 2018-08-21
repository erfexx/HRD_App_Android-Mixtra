package com.mit.mobile.absencehrd.Model;


/**
 * Created by KAYANGAN on 3/1/2018.
 */

public class AttendanceRecord {

    private String timestamp;
    private String modifiedDate;
    private String checkTime;
    private String employeeID;
    private String AttendanceType;
    private String id;

    public AttendanceRecord() {
    }

    public AttendanceRecord(String modifiedDate, String checkTime, String attendanceType) {
        this.modifiedDate = modifiedDate;
        this.checkTime = checkTime;
        AttendanceType = attendanceType;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getAttendanceType() {
        return AttendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        AttendanceType = attendanceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
