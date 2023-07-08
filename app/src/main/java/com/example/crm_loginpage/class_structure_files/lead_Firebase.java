package com.example.crm_loginpage.class_structure_files;

public class lead_Firebase {
    String cmp_nm;
    String ct_pr;
    String ct_ph;
    String mail;
    String f_dt;
    String f_tm;
    String l_owner;
    String l_status;
    String l_add;
    String city;
    String state;
    String pinc;
    String sno;
    String id;
    address_firebase adr;


    public lead_Firebase(String cmp_nm, String ct_pr, String ct_ph, String mail, String f_dt, String f_tm, String l_owner, String l_status, String id) {
        this.cmp_nm = cmp_nm;
        this.ct_pr = ct_pr;
        this.ct_ph = ct_ph;
        this.mail = mail;
        this.f_dt = f_dt;
        this.f_tm = f_tm;
        this.l_owner = l_owner;
        this.l_status = l_status;
        this.id = id;
    }


    public lead_Firebase() {
    }

    public String getCmp_nm() {
        return cmp_nm;
    }

    public void setCmp_nm(String cmp_nm) {
        this.cmp_nm = cmp_nm;
    }

    public String getCt_pr() {
        return ct_pr;
    }

    public void setCt_pr(String ct_pr) {
        this.ct_pr = ct_pr;
    }

    public String getCt_ph() {
        return ct_ph;
    }

    public void setCt_ph(String ct_ph) {
        this.ct_ph = ct_ph;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getF_dt() {
        return f_dt;
    }

    public void setF_dt(String f_dt) {
        this.f_dt = f_dt;
    }

    public String getF_tm() {
        return f_tm;
    }

    public void setF_tm(String f_tm) {
        this.f_tm = f_tm;
    }

    public String getL_owner() {
        return l_owner;
    }

    public void setL_owner(String l_owner) {
        this.l_owner = l_owner;
    }

    public String getL_status() {
        return l_status;
    }

    public void setL_status(String l_status) {
        this.l_status = l_status;
    }

    public String getL_add() {
        return l_add;
    }

    public void setL_add(String l_add) {
        this.l_add = l_add;
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

    public String getPinc() {
        return pinc;
    }

    public void setPinc(String pinc) {
        this.pinc = pinc;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
