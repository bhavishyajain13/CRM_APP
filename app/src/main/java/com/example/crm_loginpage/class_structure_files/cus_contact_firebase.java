package com.example.crm_loginpage.class_structure_files;

public class cus_contact_firebase {
    String id;
    String name;
    String ph;
    String email;
    String dept;
    String desg;
    String cus_id;

    @Override
    public String toString() {
        return name;
    }

    public cus_contact_firebase() {
    }

    public cus_contact_firebase(String id, String name, String ph, String email, String dept, String desg) {
        this.id = id;
        this.name = name;
        this.ph = ph;
        this.email = email;
        this.dept = dept;
        this.desg = desg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }
}
