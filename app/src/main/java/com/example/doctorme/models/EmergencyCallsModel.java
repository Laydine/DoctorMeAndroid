package com.example.doctorme.models;

public class EmergencyCallsModel {
    String id,name,contact,userId,date;

    public EmergencyCallsModel() {
    }

    public String getUserId() {
        return userId;
    }

    public EmergencyCallsModel(String id, String name, String contact, String userId, String date) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.userId = userId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getDate() {
        return date;
    }
}
