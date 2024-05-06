package services;

import models.Event;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvent {
    private Connection connection;

    public ServiceEvent() {
        try {
            this.connection = DBConnection.getInstance().getCnx();
            if (this.connection == null) {
                throw new SQLException("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection could not be initialized.", e);
        }
    }

    public void addEvent(Event event) throws SQLException {
        if (event == null || event.getTitre() == null || event.getLocalisation() == null || event.getDate() == null) {
            throw new IllegalArgumentException("Event details cannot be null.");
        }
        String query = "INSERT INTO event (titre, lieu, date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, event.getTitre());
            preparedStatement.setString(2, event.getLocalisation());
            preparedStatement.setString(3, event.getDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT id, titre, lieu, date FROM event";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Event event = new Event();
                event.setId(resultSet.getInt("id"));
                event.setTitre(resultSet.getString("titre"));
                event.setLocalisation(resultSet.getString("lieu"));
                event.setDate(resultSet.getString("date"));
                events.add(event);
            }
        }
        return events;
    }

    public void updateEvent(Event event) throws SQLException {
        if (event == null || event.getId() == 0) {
            throw new IllegalArgumentException("Event ID is required for update.");
        }
        String query = "UPDATE event SET titre = ?, lieu = ?, date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, event.getTitre());
            preparedStatement.setString(2, event.getLocalisation());
            preparedStatement.setString(3, event.getDate());
            preparedStatement.setInt(4, event.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        String query = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
        }
    }
}
