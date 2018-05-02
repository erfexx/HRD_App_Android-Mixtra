package com.example.kayangan.absencehrd.Model;

/**
 * Created by Kevin E on 4/25/2018.
 */

public class SalesOrder {
    int _id;
    String startdate;
    String enddate;
    String salesman;
    int voucher;

    // Empty constructor
    public SalesOrder(){

    }

    //constructor
    public SalesOrder (int id, String startdate, String enddate, String salesman, int voucher){
        this._id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.salesman = salesman;
        this.voucher = voucher;
    }

    //constructor
    public SalesOrder (String startdate, String enddate, String salesman, int voucher){
        this.startdate = startdate;
        this.enddate = enddate;
        this.salesman = salesman;
        this.voucher = voucher;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public int getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
        this.voucher = voucher;
    }
}
