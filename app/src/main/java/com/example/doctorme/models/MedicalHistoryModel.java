package com.example.doctorme.models;

public class MedicalHistoryModel {
    String id, patientId,illness,symptoms,medicines,date;

    public MedicalHistoryModel() {
    }

    public MedicalHistoryModel(String id, String userId, String illness, String symptoms, String medicines, String date) {
        this.id = id;
        this.patientId = userId;
        this.illness = illness;
        this.symptoms = symptoms;
        this.medicines = medicines;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getIllness() {
        return illness;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getDate() {
        return date;
    }
}
