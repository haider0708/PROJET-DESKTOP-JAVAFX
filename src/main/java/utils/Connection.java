package utils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    private static final String URL = "jdbc:mysql://localhost:3306/integration";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection instance;
    private java.sql.Connection cnx;

    // Make the constructor private to prevent instantiation from outside
    private Connection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            // Handle exception appropriately, such as logging or throwing a runtime exception
            throw new RuntimeException("Failed to connect to the database.", e);
        }
    }

    // Make getInstance() method synchronized for thread safety
    public static synchronized Connection getInstance() {
        if (instance == null)
            instance = new Connection();
        return instance;
    }

    public java.sql.Connection getCnx() {
        return cnx;
    }
}