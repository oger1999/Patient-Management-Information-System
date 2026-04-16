
package com.pmis.dao;

import com.pmis.model.Patient;
import java.time.LocalDate;
import java.util.List;


public interface PatientDao {
    String addPatient(Patient patient);
    List<Patient> getAllPatients();
    Patient getPatientById(String PatientID);
    List<Patient> getPatientsByDate(LocalDate registrationDate);
    String updatePatient(String PatientID , Patient patient);
    String deletePatient(String PatientID);
    List<Patient> searchByName(String patientName);
}
