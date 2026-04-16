
package com.pmis.model;

import java.time.LocalDate;


public class Patient {
   private String PatientID;
   private String fullName;
   private int age;
   private String gender;
   private String diagnosis;
   private double consultationFee;
   private LocalDate registrationDate;

    public Patient() {
    }

    public Patient(String PatientID, String fullName, int age, String gender, String diagnosis, double consultationFee, LocalDate registrationDate) {
        this.PatientID = PatientID;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.diagnosis = diagnosis;
        this.consultationFee = consultationFee;
        this.registrationDate = registrationDate;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String PatientID) {
        this.PatientID = PatientID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

 
   
}
