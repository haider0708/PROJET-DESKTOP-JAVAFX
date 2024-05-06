package services;

import models.Patient;
import utils.Connection;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Service {

    private static java.sql.Connection connection = null;

    public Service() {
        connection = Connection.getInstance().getCnx();
    }

    public static void ajouter(Patient patient) throws SQLException {
        String query = "INSERT INTO patient (email, roles, password, firstname, lastname, sexe, age, number, img_path, address, is_verified, reset_token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patient.getEmail());
            preparedStatement.setString(2, String.join(",", patient.getRoles()));
            preparedStatement.setString(3, patient.getPassword());
            preparedStatement.setString(4, patient.getFirstname());
            preparedStatement.setString(5, patient.getLastname());
            preparedStatement.setString(6, patient.getSexe());
            preparedStatement.setInt(7, patient.getAge());
            preparedStatement.setString(8, patient.getNumber());
            preparedStatement.setString(9, patient.getImg_path());
            preparedStatement.setString(10, patient.getAddress());
            preparedStatement.setBoolean(11, patient.getIs_verified());
            preparedStatement.setString(12, patient.getReset_token());
            preparedStatement.executeUpdate();
        }
    }

    public void update(Patient updatedPatient) throws SQLException {
        String query = "UPDATE patient SET email = ?, roles = ?, password = ?, firstname = ?, lastname = ?, sexe = ?, age = ?, number = ?, img_path = ?, address = ?, is_verified = ?, reset_token = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, updatedPatient.getEmail());
            preparedStatement.setString(2, String.join(",", updatedPatient.getRoles()));
            preparedStatement.setString(3, updatedPatient.getPassword());
            preparedStatement.setString(4, updatedPatient.getFirstname());
            preparedStatement.setString(5, updatedPatient.getLastname());
            preparedStatement.setString(6, updatedPatient.getSexe());
            preparedStatement.setInt(7, updatedPatient.getAge());
            preparedStatement.setString(8, updatedPatient.getNumber());
            preparedStatement.setString(9, updatedPatient.getImg_path());
            preparedStatement.setString(10, updatedPatient.getAddress());
            preparedStatement.setBoolean(11, updatedPatient.getIs_verified());
            preparedStatement.setString(12, updatedPatient.getReset_token());
            preparedStatement.setInt(13, updatedPatient.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM patient WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    public ArrayList<Patient> afficherAll() throws SQLException {
        ArrayList<Patient> Patients = new ArrayList<>();
        String query = "SELECT * FROM patient";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String[] roles = resultSet.getString("roles").split(",");
                Gson gson = new Gson();
                String rolesJson = gson.toJson(roles);
                Patient patient = new Patient(resultSet.getInt("id"),
                        resultSet.getString("email"),
                        rolesJson,
                        resultSet.getString("password"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("sexe"),
                        resultSet.getInt("age"),
                        resultSet.getString("number"),
                        resultSet.getString("img_path"),
                        resultSet.getString("address"),
                        resultSet.getBoolean("is_verified"),
                        resultSet.getString("reset_token"));

                Patients.add(patient);
            }
        }
        return Patients;
    }


  /*  public boolean login(String email, String password) throws SQLException {
        String query = "SELECT password FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    return BCrypt.checkpw(password, hashedPasswordFromDB);
                }
            }
        }
        return false;
    }*/


    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    System.out.println(count);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public Patient Log(String email, String password) throws SQLException {
        String query = "SELECT * FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        System.out.println("login");
                        Patient patient = new Patient();
                        patient.setId(resultSet.getInt("id"));
                        patient.setEmail(resultSet.getString("email"));
                        patient.setRoles(resultSet.getString("roles"));
                        patient.setPassword(resultSet.getString("password"));
                        patient.setFirstname(resultSet.getString("firstname"));
                        patient.setLastname(resultSet.getString("lastname"));
                        patient.setSexe(resultSet.getString("sexe"));
                        patient.setAge(resultSet.getInt("age"));
                        patient.setNumber(resultSet.getString("number"));
                        patient.setImg_path(resultSet.getString("img_path"));
                        patient.setAddress(resultSet.getString("address"));
                        patient.setIs_verified(resultSet.getBoolean("is_verified"));
                        patient.setResetToken(resultSet.getString("reset_token"));
                        return patient;
                    }
                    else System.out.println("LOGIN FAILED");
                }
            }
        }
        return null;
    }

    public Patient getPatientByEmail(String email) throws SQLException {
        String query = "SELECT * FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setId(resultSet.getInt("id"));
                    patient.setEmail(resultSet.getString("email"));
                    patient.setRoles(resultSet.getString("roles"));
                    patient.setPassword(resultSet.getString("password"));
                    patient.setFirstname(resultSet.getString("firstname"));
                    patient.setLastname(resultSet.getString("lastname"));
                    patient.setSexe(resultSet.getString("sexe"));
                    patient.setAge(resultSet.getInt("age"));
                    patient.setNumber(resultSet.getString("number"));
                    patient.setImg_path(resultSet.getString("img_path"));
                    patient.setAddress(resultSet.getString("address"));
                    patient.setIs_verified(resultSet.getBoolean("is_verified"));
                    patient.setResetToken(resultSet.getString("reset_token"));
                    return patient;
                }
            }
        }
        return null;
    }

    public Map<String, Long> getRoleCounts() {
        Map<String, Long> roleCounts = new HashMap<>();

        try {
            ArrayList<Patient> patients = afficherAll();

            for (Patient patient : patients) {
                String role = patient.getRoles(); // Assuming getRoles() returns a single role string

                // Increment the count for each role
                roleCounts.put(role, roleCounts.getOrDefault(role, 0L) + 1);
            }
        } catch (SQLException e) {
            // Handle any exceptions, such as database connection errors
            e.printStackTrace();
        }

        return roleCounts;
    }

}
