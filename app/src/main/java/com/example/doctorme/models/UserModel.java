package com.example.doctorme.models;

public class UserModel {
    String id,regNo,name,address,school,password,date,role;
    public UserModel() {
    }

    public UserModel(String id, String regNo, String name, String address, String school, String password, String date, String role) {
        this.id = id;
        this.regNo = regNo;
        this.name = name;
        this.address = address;
        this.school = school;
        this.password = password;
        this.date = date;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getSchool() {
        return school;
    }

    public String getDate() {
        return date;
    }

    public String getRole() {
        return role;
    }
}
