package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/integration";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static ConnectionDB instance;
    private Connection cnx;

    private ConnectionDB() {
        try {
            this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/integration", "root", "");
            System.out.println("Connected To DATABASE !");
        } catch (SQLException var2) {
            SQLException e = var2;
            System.err.println("Error: " + e.getMessage());
        }

    }

    public static ConnectionDB getInstance() {
        if (instance == null) {
            instance = new ConnectionDB();
        }

        return instance;
    }

    public static Connection getCon() {
        return getInstance().getCnx();
    }

    public Connection getCnx() {
        return this.cnx;
    }
}