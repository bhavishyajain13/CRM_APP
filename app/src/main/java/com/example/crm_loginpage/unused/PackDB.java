package com.example.crm_loginpage.unused;

public class PackDB {
    private int id;
    private int qty;
    private String descr;

    public PackDB(int id, int qty, String descr) {
        this.id = id;
        this.qty = qty;
        this.descr = descr;
    }

    public PackDB() {
    }

    @Override
    public String toString() {
        return "PackDB{" +
                "id=" + id +
                ", qty=" + qty +
                ", descr='" + descr + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
