package com.mit.mobile.absencehrd.Model;

/**
 * Created by Kevin E on 4/25/2018.
 */

public class SalesOrder {
    int _id;
    String transdate;
    String salesman;
    int voucher;

    // Empty constructor
    public SalesOrder(){

    }

    //constructor
    public SalesOrder (int id, String transdate, String salesman, int voucher){
        this._id = id;
        this.transdate = transdate;
        this.salesman = salesman;
        this.voucher = voucher;
    }

    //constructor
    public SalesOrder (String transdate, String salesman, int voucher){
        this.transdate = transdate;
        this.salesman = salesman;
        this.voucher = voucher;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTransdate() {
        return transdate;
    }

    public void setTransdate(String transdate) {
        this.transdate = transdate;
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
