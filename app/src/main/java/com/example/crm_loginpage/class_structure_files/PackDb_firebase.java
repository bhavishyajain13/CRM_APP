package com.example.crm_loginpage.class_structure_files;

public class PackDb_firebase {

    String id;
    int qty;
    String Desc;

    public PackDb_firebase(String id, int qty, String desc) {
        this.id = id;
        this.qty = qty;
        Desc = desc;
    }

    public PackDb_firebase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
