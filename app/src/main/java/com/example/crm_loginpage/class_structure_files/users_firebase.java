package com.example.crm_loginpage.class_structure_files;

public class users_firebase {
    String name;
    String phone_no;
    String mail_id;

    public users_firebase(String name, String phone_no,String mail_id) {
        this.name = name;
        this.phone_no = phone_no;
        this.mail_id = mail_id;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public users_firebase() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
