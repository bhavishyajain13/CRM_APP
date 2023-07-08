package com.example.crm_loginpage.class_structure_files;

public class GstDb_firebase {
    int gstper;
    String id;


    public GstDb_firebase(int gstper, String id) {
        this.gstper = gstper;
        this.id = id;
    }

    public GstDb_firebase() {
    }

    public int getGstper() {
        return gstper;
    }

    public void setGstper(int gstper) {
        this.gstper = gstper;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
