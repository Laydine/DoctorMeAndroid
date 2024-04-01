package com.example.doctorme.models;

public class RequestModel {
    String id,userId,message,date,reasonForDenial;
    Boolean approved;

    public RequestModel() {
    }

    public RequestModel(String id, String userId, String message, String date, String reasonForDenial, Boolean approved) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.date = date;
        this.reasonForDenial = reasonForDenial;
        this.approved = approved;
    }

    public String getReasonForDenial() {
        return reasonForDenial;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public Boolean getApproved() {
        return approved;
    }
}
