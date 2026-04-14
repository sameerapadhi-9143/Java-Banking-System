package com.example.testjdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bank_system",
                    "root",
                    "root"
            );

        } catch (Exception e) {
            System.out.println("Database Connection Failed");
        }
        return null;
    }
}