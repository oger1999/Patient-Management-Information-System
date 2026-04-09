package com.springBoot.example.Entities;

import java.time.LocalDate;
import java.util.List;

public class Patient {

    private String PatientID;
    private String FirstName;
    private String LastName;
    private String Gender;
    private String Age;
    private String Diagnosis;
    private double ConsultationFees;
    private LocalDate RegistrationDate;

    private List<Guardian> guardians;

    public Patient(String patientID, String firstName, String lastName, String gender, String age, String diagnosis, double consultationFees, LocalDate registrationDate, List<Guardian> guardians) {
       this.PatientID = patientID;
       this.FirstName = firstName;
       this.LastName = lastName;
       this.Gender = gender;
       this.Age = age;
       this.Diagnosis = diagnosis;
       this.ConsultationFees = consultationFees;
       this.RegistrationDate = registrationDate;
       this.guardians = guardians;
    }
    public Patient() {

    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public double getConsultationFees() {
        return ConsultationFees;
    }

    public void setConsultationFees(double consultationFees) {
        ConsultationFees = consultationFees;
    }

    public LocalDate getRegistrationDate() {
        return RegistrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        RegistrationDate = registrationDate;
    }

    public List<Guardian> getGuardians() {
        return guardians;
    }

    public void setGuardians(List<Guardian> guardians) {
        this.guardians = guardians;
    }


}
