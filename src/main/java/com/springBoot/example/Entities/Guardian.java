package com.springBoot.example.Entities;

public class Guardian {

    private int id;
    private String  FirstName;
    private String  LastName;
    private String Contact;
    private String Relationship;
    private Boolean isEmergency;
    private String PatientID;

    public Guardian(int id, String firstName, String lastName, String contact, String relationship, Boolean isEmergency, String patientID) {
        this.id = id;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Contact = contact;
        this.Relationship = relationship;
        this.isEmergency = isEmergency;
        this.PatientID = patientID;
    }
    public Guardian() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getRelationship() {
        return Relationship;
    }

    public void setRelationship(String relationship) {
        Relationship = relationship;
    }

    public Boolean getEmergency() {
        return isEmergency;
    }

    public void setEmergency(Boolean emergency) {
        isEmergency = emergency;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
}
