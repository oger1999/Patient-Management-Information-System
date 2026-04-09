package com.springBoot.example.DAO;

import com.springBoot.example.Database.DBconnection;
import com.springBoot.example.Entities.Patient;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PatientDaoImpl  implements PatientDao {

    private final Connection connection;

    public PatientDaoImpl() throws SQLException {
        this.connection = DBconnection.getConnection();
    }

    @Override
    public String addPatient(Patient patient) {
        return "";
    }

    @Override
    public List<Patient> getAllPatients() {
        return List.of();
    }

    @Override
    public List<Patient> getPatientsByDate(LocalDate registrationDate) {
        return List.of();
    }

    @Override
    public Patient getPatientById(String patientID) {
        return null;
    }

    @Override
    public String updatePatient(String patientID, Patient patient) {
        return "";
    }

    @Override
    public String deletePatient(String patientID) {
        return "";
    }
}
