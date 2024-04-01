package com.example.doctorme.models;

public class EmergencyInformationModel {
    String id,userId,bloodGroup,allergies,info,date;

    public EmergencyInformationModel() {
    }

    public EmergencyInformationModel(String id, String userId, String bloodGroup, String allergies, String info, String date) {
        this.id = id;
        this.userId = userId;
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
        this.info = info;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }
}
