
package com.pmis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBconnection {
    public static Connection getConnection(){
      String url = "jdbc:mysql://localhost:3306/pmis_db";
      String user = "root";
      String password = "root";
      
     try{
         Connection con = DriverManager.getConnection(url, user, password);
         return con;
     }catch(SQLException ex){
         ex.printStackTrace();
         return null;
     }
    }
    
}
