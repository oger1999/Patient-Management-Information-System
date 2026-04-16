
package com.pmis.dao;

import com.pmis.model.Guardian;
import com.pmis.util.DBconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class GuardianDaoImpl implements GuardianDao {
   private Connection con;
   
   public GuardianDaoImpl(){
       this.con = DBconnection.getConnection();
   }

    @Override
    public String addGuardian(Guardian guardian) {
        try{ 
            String sql = "INSERT INTO guardian(GuardianID,fullName,phone,relationship,PatientID) VALUE (?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, guardian.getGuardianID());
            pst.setString(2, guardian.getFullName());
            pst.setString(3, guardian.getPhone());
            pst.setString(4, guardian.getRelationship());
            pst.setString(5, guardian.getPatientID());
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            if(rowsAffected > 0){
                return "Guardian added Succefully";
            }else {
                return "Guardian not added";
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            return "Error : " + ex.getMessage();
        }
        
    }

    @Override
    public List<Guardian> getAllGuardians() {
        List <Guardian> guardianList = new ArrayList <>();
        try{
            String sql = "SELECT * FROM guardian";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Guardian guardian = new Guardian();
                guardian.setGuardianID(rs.getString("GuardianID"));
                guardian.setFullName(rs.getString("fullName"));
                guardian.setPhone(rs.getString("phone"));
                guardian.setRelationship(rs.getString("relationship"));
                guardian.setPatientID(rs.getString("PatientID"));
                
                guardianList.add(guardian);                
            }
            con.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return guardianList;
    }

    @Override
    public Guardian getGuardianById(String GuardianID) {
        Guardian guardian = new Guardian();
        try{
            String sql = "SELECT * FROM guardian WHERE GuardianID = ? ";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, GuardianID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                guardian.setGuardianID(rs.getString("GuardianID"));
                guardian.setFullName(rs.getString("fullName"));
                guardian.setPhone(rs.getString("phone"));
                guardian.setRelationship(rs.getString("relationship"));
                guardian.setPatientID(rs.getString("PatientID"));
                
                con.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return guardian;
    }

    @Override
    public List<Guardian> getGuardiansByPatientID(String PatientID) {
        List<Guardian> guardianList = new ArrayList<>();
        try{
            String sql = "SELECT * FROM guardian WHERE PatientID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, PatientID);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Guardian guardian = new Guardian();
                guardian.setGuardianID(rs.getString("GuardianID"));
                guardian.setFullName(rs.getString("fullName"));
                guardian.setPhone(rs.getString("phone"));
                guardian.setRelationship(rs.getString("relationship"));
                
                guardianList.add(guardian);
            }
            con.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return guardianList;
    }

    @Override
    public String updateGuardian(String GuardianID, Guardian guardian) {
        try{
            String sql = "UPDATE guardian SET fullName = ? , phone = ? ,relationship = ?, PatientID = ? WHERE GuardianID = ? ";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, guardian.getFullName());
            pst.setString(2, guardian.getPhone());
            pst.setString(3, guardian.getRelationship());
            pst.setString(4, guardian.getPatientID());
            pst.setString(5, GuardianID);
            
            int rowsAffected = pst.executeUpdate();
            con.close();
            if(rowsAffected > 0){
                return "Guardian Succefull Updated";
            }else {
                return "Guardian not Updated";
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return GuardianID;
    }

    @Override
    public String deleteGuardian(String GuardianID) {
        try{
            String sql = "DELETE  FROM guardian WHERE GuardianID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, GuardianID);
            int rowsAffected = pst.executeUpdate();
            con.close();
            if(rowsAffected > 0){
                return "Guardian succefull deleted";
            }else {
                return "Guardian not deleted";
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return GuardianID;
    }
    
    @Override
    public List<Guardian> searchByName(String guardianName) {
        List<Guardian> guardians = new ArrayList<>();
        try{
            String sql = "SELECT * FROM guardian WHERE fullName LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + guardianName + "%");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Guardian guardian = new Guardian();
                guardian.setGuardianID(rs.getString("GuardianID"));
                guardian.setFullName(rs.getString("fullName"));
                guardian.setPhone(rs.getString("phone"));
                guardian.setRelationship(rs.getString("relationship"));
                guardian.setPatientID(rs.getString("PatientID"));
                
                guardians.add(guardian);
            }
            con.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return guardians;
    }
}
