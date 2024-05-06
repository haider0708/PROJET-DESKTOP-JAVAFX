package services;

import models.Activity;
import models.Event;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivity {

    private final Connection connection = DBConnection.getInstance().getCnx();;

    public List<Activity> getAllActivitiesWithEventNames() throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT a.*, e.titre as eventName FROM activity a LEFT JOIN event e ON a.evenement_id = e.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activity activity = new Activity();
                activity.setId(resultSet.getInt("id"));
                activity.setNom(resultSet.getString("nom"));
                activity.setStatus(resultSet.getString("status"));
                activity.setDescription(resultSet.getString("description"));
                activity.setOrganisateur(resultSet.getString("organisateur"));
                activity.setEvenementId(resultSet.getInt("evenement_id"));
                activity.setEventName(resultSet.getString("eventName"));
                activities.add(activity);
            }
        }
        return activities;
    }

    public ServiceActivity()  {

    }

    public void addActivity(Activity activity) throws SQLException {
        String query = "INSERT INTO activity (nom, status, description, organisateur, evenement_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, activity.getNom());
            preparedStatement.setString(2, activity.getStatus());
            preparedStatement.setString(3, activity.getDescription());
            preparedStatement.setString(4, activity.getOrganisateur());
            preparedStatement.setInt(5, activity.getEvenementId());
            preparedStatement.executeUpdate();
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT id, titre, lieu, date FROM event";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
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

    public void updateActivity(Activity activity) throws SQLException {

        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Connection is not available.");
        }

        String query = "UPDATE activity SET nom = ?, status = ?, description = ?, organisateur = ?, evenement_id = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, activity.getNom());
            preparedStatement.setString(2, activity.getStatus());
            preparedStatement.setString(3, activity.getDescription());
            preparedStatement.setString(4, activity.getOrganisateur());
            preparedStatement.setInt(5, activity.getEvenementId());
            preparedStatement.setInt(6, activity.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating activity failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating activity: " + e.getMessage(), e);
        }
    }
    public void deleteActivity(int activityId) throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Connection is not available.");
        }

        String query = "DELETE FROM activity WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, activityId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting activity failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error deleting activity: " + e.getMessage(), e);
        }
    }

    public List<Activity> getActivitiesByEventId(int eventId) throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT a.*, e.titre as eventName FROM activity a LEFT JOIN event e ON a.evenement_id = e.id WHERE a.evenement_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activity activity = new Activity();
                activity.setId(resultSet.getInt("id"));
                activity.setNom(resultSet.getString("nom"));
                activity.setStatus(resultSet.getString("status"));
                activity.setDescription(resultSet.getString("description"));
                activity.setOrganisateur(resultSet.getString("organisateur"));
                activity.setEvenementId(resultSet.getInt("evenement_id"));
                activity.setEventName(resultSet.getString("eventName"));
                activities.add(activity);
            }
        }
        return activities;
    }

}
