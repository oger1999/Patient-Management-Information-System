package com.pmis.dao;

import com.pmis.model.Guardian;
import com.pmis.model.Patient;
import com.pmis.util.DBconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientDaoImpl implements PatientDao {

    private Connection con;

    public PatientDaoImpl() {
        this.con = DBconnection.getConnection();
    }

    @Override
    public String addPatient(Patient patient) {

        try {
            String sql = "INSERT INTO patient(PatientID ,fullName , age , gender , diagnosis , consultationFee , registrationDate) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, patient.getPatientID());
            pst.setString(2, patient.getFullName());
            pst.setInt(3, patient.getAge());
            pst.setString(4, patient.getGender());
            pst.setString(5, patient.getDiagnosis());
            pst.setDouble(6, patient.getConsultationFee());
            pst.setDate(7, java.sql.Date.valueOf(LocalDate.now()));

            int rowsAffected = pst.executeUpdate();
            con.close();
            if (rowsAffected > 0) {
                return "Patient added succefully ";
            } else {
                return "Failed to add Patient";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error : " + ex.getMessage();
        }

    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM patient";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientID(rs.getString("PatientID"));
                patient.setFullName(rs.getString("fullName"));
                patient.setGender(rs.getString("gender"));
                patient.setDiagnosis(rs.getString("diagnosis"));
                patient.setAge(rs.getInt("age"));
                patient.setConsultationFee(rs.getDouble("consultationFee"));
                patient.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());

                patientList.add(patient);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return patientList;
    }

    @Override
    public Patient getPatientById(String PatientID) {
        Patient patient = new Patient();
        try {
            String sql = "SELECT * FROM patient WHERE PatientID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, PatientID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                patient.setPatientID(rs.getString("PatientID"));
                patient.setFullName(rs.getString("fullName"));
                patient.setGender(rs.getString("gender"));
                patient.setDiagnosis(rs.getString("diagnosis"));
                patient.setAge(rs.getInt("age"));
                patient.setConsultationFee(rs.getDouble("consultationFee"));
                patient.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());

                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return patient;
    }

    @Override
    public List<Patient> getPatientsByDate(LocalDate registrationDate) {
        List<Patient> patientList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM patient WHERE registrationDate = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, java.sql.Date.valueOf(registrationDate));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientID(rs.getString("PatientID"));
                patient.setFullName(rs.getString("fullName"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setDiagnosis(rs.getString("diagnosis"));
                patient.setConsultationFee(rs.getDouble("consultationFee"));
                patient.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());

                patientList.add(patient);
            }
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return patientList;

    }

    @Override
    public String updatePatient(String PatientID, Patient patient) {
        try {
            String sql = " UPDATE patient SET fullName = ? , age = ? , gender = ?, diagnosis = ? , consultationFee = ?  WHERE PatientID = ? ";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, patient.getFullName());
            pst.setInt(2, patient.getAge());
            pst.setString(3, patient.getGender());
            pst.setString(4, patient.getDiagnosis());
            pst.setDouble(5, patient.getConsultationFee());
            pst.setString(6, PatientID);

            int rowsAffected = pst.executeUpdate();
            con.close();
            if (rowsAffected > 0) {
                return "Patient Updated Succefully";
            } else {
                return "Patient not Updated ";
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return PatientID;
    }

    @Override
    public String deletePatient(String PatientID) {
        try {
            String sql = " DELETE FROM patient WHERE PatientID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, PatientID);
            int rowsAffected = pst.executeUpdate();
            con.close();
            if (rowsAffected > 0) {
                return "Patient Successfully Deleted";
            } else {
                return "Patient not Deleted";
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return PatientID;
    }

    @Override
    public List<Patient> searchByName(String patientName) {
        List<Patient> patients = new ArrayList<>();
        try {
            String sql = "SELECT * FROM patient WHERE fullName LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + patientName + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientID(rs.getString("patientID"));
                patient.setFullName(rs.getString("fullName"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setDiagnosis(rs.getString("diagnosis"));
                patient.setConsultationFee(rs.getDouble("consultationFee"));
                patient.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());

                patients.add(patient);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return patients;
    }

}
