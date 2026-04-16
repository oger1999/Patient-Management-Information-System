
package com.pmis.dao;

import com.pmis.model.Guardian;
import java.util.List;


public interface GuardianDao {
    String addGuardian(Guardian guardian);
    List<Guardian> getAllGuardians();
    Guardian getGuardianById(String GuardianID);
    List<Guardian> getGuardiansByPatientID(String PatientID);
    String updateGuardian(String GuardianID , Guardian guardian);
    String deleteGuardian(String GuardianID);
    List<Guardian> searchByName(String guardianName);
}
