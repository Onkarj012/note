/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Onkar Jadhav
 */
public class ConnectionProvider {
    public static Connection getCon() {
        try {
            // Use the new driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/testdb", "root", "Onkar@tv");
            return con;
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
            return null;
        }
    }
}
