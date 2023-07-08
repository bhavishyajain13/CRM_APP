package com.example.crm_loginpage.class_structure_files;

public class hsn_firebase {

    String id;
    String HSN;
    int gst;

    public hsn_firebase() {
    }

    public hsn_firebase(String id, String HSN, int gst) {
        this.id = id;
        this.HSN = HSN;
        this.gst = gst;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHSN() {
        return HSN;
    }

    public void setHSN(String HSN) {
        this.HSN = HSN;
    }

    public int getGst() {
        return gst;
    }

    public void setGst(int gst) {
        this.gst = gst;
    }


}
