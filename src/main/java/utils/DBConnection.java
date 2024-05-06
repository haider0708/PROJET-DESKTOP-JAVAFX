package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/integration";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    static String driver = "com.mysql.cj.jdbc.Driver";

    private static DBConnection instance;

    private Connection cnx;

    //First Step: Rendre le constructeur privé
    private DBConnection() {
        try {
            Class.forName(driver);
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected To DATABASE !");
        } catch (SQLException |ClassNotFoundException e) {
            System.err.println("Error: "+e.getMessage());
        }
    }

    //Thrid Step: Creer une methode static pour recuperer l'instance

    public static DBConnection getInstance(){
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

    public Connection getcnx() {return cnx;
    }
}
