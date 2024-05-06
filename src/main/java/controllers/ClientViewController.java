package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.Activity;
import models.Event;
import services.ServiceActivity;
import services.ServiceEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    @FXML
    private Button btnmap;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, String> colTitle;

    @FXML
    private TableColumn<Event, String> colDate;

    @FXML
    private TableColumn<Event, String> colLocation;

    @FXML
    private ListView<String> activityListView;

    private ServiceEvent serviceEvent;
    private ServiceActivity serviceActivity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceEvent = new ServiceEvent();
        serviceActivity = new ServiceActivity();

        colTitle.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        loadEventData();

        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadActivitiesForEvent(newSelection.getId());
            }
        });
    }

    private void loadEventData() {
        try {
            ObservableList<Event> data = FXCollections.observableArrayList(serviceEvent.getAllEvents());
            eventTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadActivitiesForEvent(int eventId) {
        try {
            List<Activity> activities = serviceActivity.getActivitiesByEventId(eventId);
            ObservableList<String> activityDetails = FXCollections.observableArrayList();
            for (Activity activity : activities) {
                String displayText = String.format("%s - %s, (Organized by: %s, Status: %s)",
                        activity.getNom(),
                        activity.getDescription(),
                        activity.getOrganisateur(),
                        activity.getStatus());
                activityDetails.add(displayText);
            }
            activityListView.setItems(activityDetails);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load activities", "Unable to load activities for the selected event due to a database error.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleChatbotButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chatbot.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) eventTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot open chatbot", "Failed to load the chatbot view.");
        }
    }

    public void showmap() {
        Stage mapStage = new Stage();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        double latitude = 36.899500;
        double longitude = 10.189658;

        // HTML content with embedded Google Map using your API key
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
                    <style type="text/css">
                        html { height: 100% }
                        body { height: 100%; margin: 0; padding: 0 }
                        #map_canvas { height: 100% }
                    </style>
                    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC9SaD0tZsCcoIlUSc9r6zQnZKD6vl3z94"></script>
                    <script type="text/javascript">
                        function initialize() {
                            var mapOptions = {
                                center: new google.maps.LatLng(36.862499, 10.195556), // Central point between the two locations
                                zoom: 13,
                                mapTypeId: google.maps.MapTypeId.ROADMAP
                            };
                            var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
                            
                            // Coordinates for Esprit faculties
                            var locations = [
                                {lat: 36.899500, lng: 10.189658, title: 'Esprit Ariana'}, // Approximate location for Esprit Ariana
                                {lat: 36.86667, lng: 10.195556, title: 'Esprit Charguia'}  // Approximate location for Esprit Charguia
                            ];
                            
                            // Create markers for each location
                            locations.forEach(function(location) {
                                var marker = new google.maps.Marker({
                                    position: new google.maps.LatLng(location.lat, location.lng),
                                    map: map,
                                    title: location.title
                                });
                            });
                        }
                    </script>
                </head>
                <body onload="initialize()">
                    <div id="map_canvas" style="width:100%; height:100%"></div>
                </body>
                </html>
                """;

        webEngine.loadContent(html, "text/html");

        mapStage.setScene(new Scene(webView, 600, 500));
        mapStage.setTitle("Google Map");
        mapStage.show();
    }
    }
