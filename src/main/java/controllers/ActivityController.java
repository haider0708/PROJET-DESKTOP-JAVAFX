package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Activity;
import models.Event;
import services.ServiceActivity;
import services.ServiceEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ActivityController implements Initializable {

    @FXML
    private TableView<Activity> activityTable;
    @FXML
    private TableColumn<Activity, Integer> colActivityID;

    @FXML
    private TableColumn<Activity, String> colActivityName;
    @FXML
    private TableColumn<Activity, String> colStatus;
    @FXML
    private TableColumn<Activity, String> colDescription;
    @FXML
    private TableColumn<Activity, String> colOrganizer;
    @FXML
    private TableColumn<Activity, String> colEventName;
    @FXML
    private TextField nomField, statusField, descriptionField, organisateurField;
    @FXML
    private ComboBox<Event> eventComboBox;
    @FXML
    private Button addActivityButton;

    private ServiceActivity serviceActivity;
    private ServiceEvent serviceEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceActivity = new ServiceActivity();
        serviceEvent = new ServiceEvent();
        try {
            loadEventNames();
            setupActivityTable();
            loadActivities();
            setupSelectionModel();
        } catch (SQLException e) {
            showErrorAlert("Initialization error: " + e.getMessage());
        }
    }

    private void setupSelectionModel() {  activityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            loadActivityDetails(newSelection);
        }
    });
    }

    private void loadActivityDetails(Activity activity) {
        nomField.setText(activity.getNom());
        statusField.setText(activity.getStatus());
        descriptionField.setText(activity.getDescription());
        organisateurField.setText(activity.getOrganisateur());
        eventComboBox.getSelectionModel().select(findEventById(activity.getEvenementId()));

    }
    private Event findEventById(int eventId) {
        return eventComboBox.getItems().stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElse(null);
    }


    private void loadEventNames() throws SQLException {
        List<Event> events = serviceEvent.getAllEvents();
        eventComboBox.getItems().setAll(events);
        eventComboBox.setConverter(new StringConverter<Event>() {
            @Override
            public String toString(Event event) {
                if (event != null) {
                    return event.getTitre();
                }
                return null;
            }

            @Override
            public Event fromString(String string) {
                return eventComboBox.getItems().stream()
                        .filter(e -> e.getTitre().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }


    private void setupActivityTable() {
        colActivityID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colActivityName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colOrganizer.setCellValueFactory(new PropertyValueFactory<>("organisateur"));
        colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
    }

    private void loadActivities() {
        try {
            List<Activity> activities = serviceActivity.getAllActivitiesWithEventNames();
            activityTable.getItems().setAll(activities);
        } catch (SQLException e) {
            showErrorAlert("Failed to load activities: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();
        String nom = nomField.getText();
        String status = statusField.getText();
        String description = descriptionField.getText();
        String organisateur = organisateurField.getText();

        if (nom == null || nom.isEmpty() || nom.length() < 6 || !Character.isUpperCase(nom.charAt(0))) {
            errorMessage.append("Name must start with an uppercase letter and be at least 6 characters long.\n");
        }
        if (status == null || status.isEmpty() || !("en cours".equals(status) || "terminer".equals(status) || "annuler".equals(status))) {
            errorMessage.append("Status must be one of the following: en cours, terminer, annuler.\n");
        }
        if (description == null || description.isEmpty() || description.length() > 100 || !Character.isUpperCase(description.charAt(0))) {
            errorMessage.append("Description must start with an uppercase letter and cannot be more than 100 characters long.\n");
        }
        if (organisateur == null || organisateur.isEmpty() || organisateur.length() < 3 || !Character.isUpperCase(organisateur.charAt(0))) {
            errorMessage.append("Organizer must start with an uppercase letter and be at least 3 characters long.\n");
        }
        if (eventComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage.append("An event must be selected.\n");
        }

        if (errorMessage.length() > 0) {
            showErrorAlert(errorMessage.toString());
            return false;
        }
        return true;
    }


    @FXML
    private void handleAddActivity() {
        String nom = nomField.getText();
        String status = statusField.getText();
        String description = descriptionField.getText();
        String organisateur = organisateurField.getText();
        Event selectedEvent = eventComboBox.getSelectionModel().getSelectedItem();
        if (!validateInput()) {
            return;
        }
        if (selectedEvent != null) {
            Activity activity = new Activity(nom, status, description, organisateur, selectedEvent.getId());
            try {
                serviceActivity.addActivity(activity);
                loadActivities();
                showSuccessAlert("Activity added successfully!");
                clearFields();
            } catch (SQLException e) {
                showErrorAlert("Failed to add activity: " + e.getMessage());
            }
        } else {
            showErrorAlert("No event selected!");
        }
    }

    private void clearFields() {
        nomField.clear();
        statusField.clear();
        descriptionField.clear();
        organisateurField.clear();
        eventComboBox.getSelectionModel().clearSelection();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleUpdateActivity() {
        if (!validateInput()) {
            return;
        }
        Activity selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            updateActivityDetails(selectedActivity);
        } else {
            showErrorAlert("No activity selected for update!");
        }
    }

    private void updateActivityDetails(Activity activity) {
        try {
            activity.setNom(nomField.getText());
            activity.setStatus(statusField.getText());
            activity.setDescription(descriptionField.getText());
            activity.setOrganisateur(organisateurField.getText());
            Event selectedEvent = eventComboBox.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                activity.setEvenementId(selectedEvent.getId());
            }

            serviceActivity.updateActivity(activity);
            loadActivities();
            showSuccessAlert("Activity updated successfully!");
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Failed to update activity: " + e.getMessage());
        }
    }

    public void handleDeleteActivity() {
        Activity selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        if (selectedActivity == null) {
            showErrorAlert("No activity selected for deletion!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this activity?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait();

        if (confirmAlert.getResult() == ButtonType.YES) {
            try {
                serviceActivity.deleteActivity(selectedActivity.getId());
                loadActivities();
                showSuccessAlert("Activity deleted successfully!");
            } catch (SQLException e) {
                showErrorAlert("Failed to delete activity: " + e.getMessage());
            }
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Navigation error: " + e.getMessage());
        }
    }

}

