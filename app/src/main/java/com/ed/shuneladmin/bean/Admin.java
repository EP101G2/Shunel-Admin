package com.ed.shuneladmin.bean;

import java.io.Serializable;

public class Admin implements Serializable {

    //宣告區
    private int Admin_ID;
    private String Admin_Name;
    private String Admin_User_Name;
    private String Admin_User_Password;
    private String Admin_User_Position;


    //建構區

    public Admin(String admin_Name, String admin_User_Name, String admin_User_Password, String admin_User_Position) {
        Admin_Name = admin_Name;
        Admin_User_Name = admin_User_Name;
        Admin_User_Password = admin_User_Password;
        Admin_User_Position = admin_User_Position;
    }


    public Admin(int admin_ID, String admin_Name, String admin_User_Name, String admin_User_Password,
                 String admin_User_Position) {
        super();
        Admin_ID = admin_ID;
        Admin_Name = admin_Name;
        Admin_User_Name = admin_User_Name;
        Admin_User_Password = admin_User_Password;
        Admin_User_Position = admin_User_Position;
    }

    public Admin(int admin_ID, String admin_Name) {
        Admin_ID = admin_ID;
        Admin_Name = admin_Name;
    }

//方法區



    public int getAdmin_ID() {
        return Admin_ID;
    }

    public void setAdmin_ID(int admin_ID) {
        Admin_ID = admin_ID;
    }

    public String getAdmin_Name() {
        return Admin_Name;
    }

    public void setAdmin_Name(String admin_Name) {
        Admin_Name = admin_Name;
    }

    public String getAdmin_User_Name() {
        return Admin_User_Name;
    }

    public void setAdmin_User_Name(String admin_User_Name) {
        Admin_User_Name = admin_User_Name;
    }

    public String getAdmin_User_Password() {
        return Admin_User_Password;
    }

    public void setAdmin_User_Password(String admin_User_Password) {
        Admin_User_Password = admin_User_Password;
    }

    public String getAdmin_User_Position() {
        return Admin_User_Position;
    }

    public void setAdmin_User_Position(String admin_User_Position) {
        Admin_User_Position = admin_User_Position;
    }



}

