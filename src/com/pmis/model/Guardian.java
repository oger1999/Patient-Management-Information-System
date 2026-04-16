
package com.pmis.model;


public class Guardian {
    private String GuardianID;
    private String fullName;
    private String phone;
    private String relationship;
    private String address;
    private String PatientID;

    public Guardian() {
    }

    public Guardian(String GuardianID, String fullName, String phone, String relationship, String address, String PatientID) {
        this.GuardianID = GuardianID;
        this.fullName = fullName;
        this.phone = phone;
        this.relationship = relationship;
        this.address = address;
        this.PatientID = PatientID;
    }

    public String getGuardianID() {
        return GuardianID;
    }

    public void setGuardianID(String GuardianID) {
        this.GuardianID = GuardianID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String PatientID) {
        this.PatientID = PatientID;
    }
    
    
}
