package com.example.crm_loginpage.class_structure_files;

import java.util.List;

public class customer_firebase {
    String id;
    String leadno;
    String cus_name;
    String sno;
    String cus_type;
    List<address_firebase> adr_list;
    List<cus_contact_firebase> ct_list;

    public customer_firebase(String id, String leadno, String cus_name, String cus_type) {
        this.id = id;
        this.leadno = leadno;
        this.cus_name = cus_name;
        this.cus_type = cus_type;
    }

    public customer_firebase(String id, String leadno, String cus_name, String cus_type, List<address_firebase> Address, List<cus_contact_firebase> Contacts) {
        this.leadno = leadno;
        this.id=id;
        this.cus_name = cus_name;
        this.cus_type = cus_type;
        this.adr_list = Address;
        this.ct_list = Contacts;
    }

    public customer_firebase() {
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

    public String getLeadno() {
        return leadno;
    }

    public void setLeadno(String leadno) {
        this.leadno = leadno;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_type() {
        return cus_type;
    }

    public void setCus_type(String cus_type) {
        this.cus_type = cus_type;
    }

    public List<address_firebase> getAdr_list() {
        return adr_list;
    }

    public void setAdr_list(List<address_firebase> adr_list) {
        this.adr_list = adr_list;
    }

    public List<cus_contact_firebase> getCt_list() {
        return ct_list;
    }

    public void setCt_list(List<cus_contact_firebase> ct_list) {
        this.ct_list = ct_list;
    }
}
