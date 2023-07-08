package com.example.crm_loginpage.class_structure_files;

public class prod_firebase {
    String id;
    String pur_rate;
    String mrp_rate;
    String sale_rate;
    String p_name;
    String sel_packu;
    String sel_gst;
    String sel_hsn;
    String status;
    String sno;

    public prod_firebase(String id,String pur_rate, String mrp_rate, String sale_rate, String p_name, String sel_packu, String sel_gst, String sel_hsn,String status,String sno) {
        this.id=id;
        this.sno=sno;
        this.pur_rate = pur_rate;
        this.mrp_rate = mrp_rate;
        this.sale_rate = sale_rate;
        this.p_name = p_name;
        this.sel_packu = sel_packu;
        this.sel_gst = sel_gst;
        this.sel_hsn = sel_hsn;
        this.status=status;
    }

    public prod_firebase(String id,String pur_rate, String mrp_rate, String sale_rate, String p_name, String sel_packu, String sel_gst, String sel_hsn,String status) {
        this.id=id;
        this.sno="0";
        this.pur_rate = pur_rate;
        this.mrp_rate = mrp_rate;
        this.sale_rate = sale_rate;
        this.p_name = p_name;
        this.sel_packu = sel_packu;
        this.sel_gst = sel_gst;
        this.sel_hsn = sel_hsn;
        this.status=status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public prod_firebase() {
    }

    public String getPur_rate() {
        return pur_rate;
    }

    public void setPur_rate(String pur_rate) {
        this.pur_rate = pur_rate;
    }

    public String getMrp_rate() {
        return mrp_rate;
    }

    public void setMrp_rate(String mrp_rate) {
        this.mrp_rate = mrp_rate;
    }

    public String getSale_rate() {
        return sale_rate;
    }

    public void setSale_rate(String sale_rate) {
        this.sale_rate = sale_rate;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getSel_packu() {
        return sel_packu;
    }

    public void setSel_packu(String sel_packu) {
        this.sel_packu = sel_packu;
    }

    public String getSel_gst() {
        return sel_gst;
    }

    public void setSel_gst(String sel_gst) {
        this.sel_gst = sel_gst;
    }

    public String getSel_hsn() {
        return sel_hsn;
    }

    public void setSel_hsn(String sel_hsn) {
        this.sel_hsn = sel_hsn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
