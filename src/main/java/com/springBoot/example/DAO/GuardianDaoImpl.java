package com.springBoot.example.DAO;

import com.springBoot.example.Database.DBconnection;
import com.springBoot.example.Entities.Guardian;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GuardianDaoImpl implements GuardianDao {

    private Connection connection;

    public GuardianDaoImpl() throws SQLException {
        this.connection = DBconnection.getConnection();
    }

    @Override
    public String addGuardian(String patientID, Guardian guardian) {
        return "";
    }

    @Override
    public List<Guardian> getGuardians() {
        return List.of();
    }

    @Override
    public List<Guardian> getGuardiansByPatientID(String patientID) {
        return List.of();
    }

    @Override
    public Guardian getGuardianByPatient(String patientID) {
        return null;
    }

    @Override
    public Guardian getGuardian(String guardianID) {
        return null;
    }

    @Override
    public String updateGuardian(String guardianID, Guardian guardian) {
        return "";
    }
}
