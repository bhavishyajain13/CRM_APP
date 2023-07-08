package com.example.crm_loginpage.class_structure_files;

public class opportunity_firebase {
    String s_no;
    String id;
    String opp_name;
    String cus_id;
    String ct_name;
    String ph;
    String email;
    String f_dt;
    String exp_dt;
    String opp_own;
    String sale_stage;
    String comment;
    String cus_name;


    public opportunity_firebase(String id, String opp_name, String cus_or_lead, String ct_name, String ph, String email, String f_dt, String exp_dt, String opp_own, String sale_stage, String comment,String cus_name) {
        this.id = id;
        this.opp_name = opp_name;
        this.cus_id = cus_or_lead;
        this.ct_name = ct_name;
        this.ph = ph;
        this.email = email;
        this.f_dt = f_dt;
        this.exp_dt = exp_dt;
        this.opp_own = opp_own;
        this.sale_stage = sale_stage;
        this.comment = comment;
        this.cus_name=cus_name;
    }


    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public opportunity_firebase() {
    }

    public String getOpp_name() {
        return opp_name;
    }

    public void setOpp_name(String opp_name) {
        this.opp_name = opp_name;
    }

    public String getCus_id() {
        return cus_id;
    }

    public void setCus_id(String cus_id) {
        this.cus_id = cus_id;
    }

    public String getCt_name() {
        return ct_name;
    }

    public void setCt_name(String ct_name) {
        this.ct_name = ct_name;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getF_dt() {
        return f_dt;
    }

    public void setF_dt(String f_dt) {
        this.f_dt = f_dt;
    }

    public String getExp_dt() {
        return exp_dt;
    }

    public void setExp_dt(String exp_dt) {
        this.exp_dt = exp_dt;
    }

    public String getOpp_own() {
        return opp_own;
    }

    public void setOpp_own(String opp_own) {
        this.opp_own = opp_own;
    }

    public String getSale_stage() {
        return sale_stage;
    }

    public void setSale_stage(String sale_stage) {
        this.sale_stage = sale_stage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
