package com.example.crm_loginpage.class_structure_files;

public class address_firebase {
    String address;
    String city;
    String state;
    String pincode;
    String sno;
    String id;





    public address_firebase(String address, String city, String state, String pincode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public address_firebase(String id,String address, String city, String state, String pincode) {
        this.id=id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }
    public address_firebase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
