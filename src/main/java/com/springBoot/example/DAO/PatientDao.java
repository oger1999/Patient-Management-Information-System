package com.springBoot.example.DAO;

import com.springBoot.example.Entities.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientDao {

    String addPatient(Patient patient);
    List<Patient> getAllPatients();
    List<Patient> getPatientsByDate(LocalDate registrationDate);
    Patient getPatientById(String patientID);
    String updatePatient( String patientID, Patient patient);
    String deletePatient(String patientID);
}
