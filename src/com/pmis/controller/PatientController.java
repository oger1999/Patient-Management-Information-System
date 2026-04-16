
package com.pmis.controller;

import com.pmis.dao.PatientDao;
import com.pmis.dao.PatientDaoImpl;
import com.pmis.model.Patient;
import static java.util.Collections.list;
import java.util.List;


public class PatientController {
    
    private PatientDao patientDao;
    public PatientController () {
        patientDao = new PatientDaoImpl();
    }
    public String addPatient(Patient patient){      
        return patientDao.addPatient(patient);
        
    }
    public List <Patient> getAllPatients (){
     
        return patientDao.getAllPatients();
       
    }
    public Patient getPatientById(String patientID){
        if (patientID == null || patientID.isEmpty()){
            return null;
        }
        return patientDao.getPatientById(patientID);
    }
    public String updatePatient(String patientID, Patient patient) {
       if(patientID == null || patientID.isEmpty()){
           return "Patient ID is required";
       } 
       if(!patientID.matches("PAT-\\d{4}")){
           return "Invalid Patient ID";
       }
       Patient existingPatient = patientDao.getPatientById(patientID);
       if(existingPatient == null){
           return "Patient not found";
       }
       if(patient.getFullName()== null || patient.getFullName().isEmpty()){
           return "Patient Name is required";
       }
       if(patient.getDiagnosis()== null || patient.getFullName().isEmpty()){
           return "Patient Diagnosis is required";
       }
       if (patient.getFullName().length() < 5){
           return "Patient name must be at least 5 character";
       }
       if(patient.getDiagnosis().length() < 5){
           return "Patient Diagnosis must be at least 5charcter";
       }
       if(patient.getAge() < 0 || patient.getAge() > 120){
           return "Patient age must be  between 0 and 120";
       }
       if (patient.getConsultationFee() < 5000 || patient.getConsultationFee()>50000){
           return "Patient consultation must be between 5000 and 50000";
       }
       double fee = patient.getConsultationFee();
       if(patient.getAge() <12){
           fee = fee * 0.5;
       } else if (patient.getAge() > 60){
           fee = fee * 0.7;
       }
       patient.setConsultationFee(fee);
       patient.setPatientID(patientID);
       return patientDao.updatePatient(patientID, patient);
       
    }
    
    public String deletePatient(String patientID){
        if(patientID == null || patientID.isEmpty()){
            return "Patient ID is required";
        }
        return patientDao.deletePatient(patientID);
    }
    
    public List<Patient> searchByName(String patientName) {
        return patientDao.searchByName(patientName);
    }
}
