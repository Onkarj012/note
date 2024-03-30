/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Note;
import java.sql.*;

/**
 *
 * @author Onkar Jadhav
 */
public class Notedao {
    private Connection connection;
    
    public Notedao() {
        connectToDatabase();
    }
    
    private void connectToDatabase() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            String url = "jdbc:mysql://localhost:3307/testdb";
            String user = "root";
            String password = "Onkar@tv";
            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int insert(Note note) {
        int noteId = -1; // Initialize noteId to -1 (invalid ID)
        
        try {
            // Prepare the SQL statement to insert a new note
            String sql = "INSERT INTO PersonalNotes (title, content, creation_datetime, last_edit_datetime) VALUES (?, ?, NOW(), NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            // Set the parameters for the prepared statement
            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            
            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();
            
            // Check if the insert was successful
            if (rowsAffected > 0) {
                // Get the generated keys (in this case, just one key)
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                
                if (generatedKeys.next()) {
                    // Retrieve the value of the auto-generated key (note_id)
                    noteId = generatedKeys.getInt(1);
                    System.out.println("Note inserted with ID: " + noteId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return noteId;
    }

    public void update(Note note) {
        try {
            // Prepare the SQL statement to update an existing note
            String sql = "UPDATE PersonalNotes SET title = ?, content = ?, last_edit_datetime = NOW() WHERE note_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // Set the parameters for the prepared statement
            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            preparedStatement.setInt(3, note.getnoteID());
            
            // Execute the update statement
            int rowsAffected = preparedStatement.executeUpdate();
            
            // Check if the update was successful
            if (rowsAffected > 0) {
                System.out.println("Note updated successfully.");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean deleteNote(Note note) {
        try {
            // Prepare the SQL statement to delete an existing note
            String sql = "DELETE FROM PersonalNotes WHERE note_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, note.getnoteID());
            
            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();
            
            // Check if the delete was successful
            if (rowsAffected > 0) {
                System.out.println("Note deleted successfully.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Note getNoteById(int noteId) {
        Note note = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            String sql = "SELECT * FROM PersonalNotes WHERE note_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, noteId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                note = new Note();
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return note;
    }
}
