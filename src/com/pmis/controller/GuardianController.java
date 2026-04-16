
package com.pmis.controller;

import com.pmis.dao.GuardianDao;
import com.pmis.dao.GuardianDaoImpl;
import com.pmis.dao.PatientDao;
import com.pmis.dao.PatientDaoImpl;
import com.pmis.model.Patient;
import com.pmis.model.Guardian;
import java.util.List;


public class GuardianController {
    private GuardianDao guardianDao;
    private PatientDao patientDao;
    public GuardianController (){
        guardianDao = new GuardianDaoImpl();
        patientDao = new PatientDaoImpl();
    }
    public String addGuardian(Guardian guardian){
        if(guardian.getGuardianID()== null || guardian.getGuardianID().isEmpty()){
            return "Guardian ID is required";
        }
        if(guardian.getFullName()== null || guardian.getFullName().isEmpty()){
            return "Guardian Name is required";
        }
        if (guardian.getPatientID()== null || guardian.getPatientID().isEmpty()){
            return "Patient ID is required";
        }
        if(!guardian.getGuardianID().matches("GUA-\\d{4}")){
            return "Invalid Guardian ID";
        }
        if(guardian.getFullName().length()< 5){
            return "Guardian name must be at least 5 charcter";
        }
        Patient patient = patientDao.getPatientById(guardian.getPatientID());
        if (patient == null) {
            return "Patient not found";
        }
        return guardianDao.addGuardian(guardian);
    }
    public List<Guardian> getAllGuardians() {
        return guardianDao.getAllGuardians();
    }
    public List <Guardian>getGuardianByPatientID(String patientID){
        if(patientID == null || patientID.isEmpty()){
            return null;
        }
        Patient patient = patientDao.getPatientById(patientID);
        if(patient == null){
            return null;
        }
        return guardianDao.getGuardiansByPatientID(patientID);
    }
    public String updateGuardian(String guardianID, Guardian guardian) {
        if (guardian.getGuardianID()== null || guardian.getGuardianID().isEmpty()){
            return "Guardian ID is required";
        }
        if(guardian.getFullName()== null || guardian.getFullName().isEmpty()){
            return "Guardian Name is required";
        }
        if(!guardianID.matches("GUA-\\d{4}")){
            return "Invalid Guardian ID";
        }
        if(guardian.getFullName().length()< 5){
            return "Guardian name must be at least 5 character";
        }
        Patient patient = patientDao.getPatientById(guardian.getPatientID());
        if(patient == null){
            return "Patient not found";
        }
        return guardianDao.updateGuardian(guardianID, guardian);
    }
    public String deleteGuardian(String GuardianID){
        if(GuardianID == null || GuardianID.isEmpty()){
            return "Guardian ID is required";
        }
        return guardianDao.deleteGuardian(GuardianID);
    }
    public List<Guardian> searchByName(String guardianName) {
        return guardianDao.searchByName(guardianName);
    }
}
