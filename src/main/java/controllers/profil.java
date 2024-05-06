package controllers;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;
import models.Patient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class profil implements Initializable {

    @FXML
    private BorderPane Pane;

    @FXML
    private Pane pane2;

    @FXML
    void Back(MouseEvent event) {
        Pane.setVisible(false);
        // pane2.getChildren().clear();
    }

    @FXML
    void EditProfil(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfil.fxml"));
            Pane addUserView = loader.load();
            pane2.getChildren().setAll(addUserView);
            pane2.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void Logout(MouseEvent event) {
        try {
            SessionManager.endSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene loginScene = new Scene(root);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(loginScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void Security(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
            Pane addUserView = loader.load();
            pane2.getChildren().setAll(addUserView);
            pane2.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Patient currentPatient = SessionManager.getCurrentSession();
    }
}
