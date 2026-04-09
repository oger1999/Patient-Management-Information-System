package com.springBoot.example.Database;

import jdk.internal.logger.SurrogateLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private final static String url ="";
    private final static String user ="";
    private final static String password ="";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
}
