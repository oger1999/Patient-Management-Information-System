package com.springBoot.example.DAO;

import com.springBoot.example.Entities.Guardian;

import java.util.List;

public interface GuardianDao {
    String addGuardian(String patientID, Guardian guardian);
    List<Guardian> getGuardians();
    List<Guardian> getGuardiansByPatientID(String patientID);
    Guardian getGuardianByPatient(String patientID);
    Guardian getGuardian(String guardianID);
    String updateGuardian(String guardianID, Guardian guardian);
}
