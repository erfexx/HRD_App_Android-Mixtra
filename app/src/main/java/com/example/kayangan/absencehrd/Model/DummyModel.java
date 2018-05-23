package com.example.kayangan.absencehrd.Model;

public class DummyModel {
    private String item, category, branch, department;
    int price;

    public DummyModel() {
    }

    public DummyModel(String item, String category, String branch, String department, int price) {
        this.item = item;
        this.category = category;
        this.branch = branch;
        this.department = department;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
