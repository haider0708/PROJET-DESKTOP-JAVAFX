package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import tray.notification.NotificationType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TwoFactorAuth implements Initializable {

    @FXML
    private Text errorTEXT;

    @FXML
    private Button loginButton;

    @FXML
    private TextField twoFAfield;

    public int attempts = 3 ;


    @FXML
    void handleLoginButtonAction(ActionEvent event) throws IOException {
        String enteredCode = twoFAfield.getText();
        int savedCode = SessionManager.getCode();
        if (String.valueOf(savedCode).equals(enteredCode)) {
            System.out.println("2FA code validated successfully!");
            NotificationApp.showNotification("Success", "2FA code validated successfully", NotificationType.SUCCESS);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) twoFAfield.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            attempts--;
            if (attempts > 0) {
                System.out.println("Invalid 2FA code. Please try again. You have " + attempts + " attempts left.");
                NotificationApp.showNotification("Invalid 2FA code ", " Please try again. You have "+ attempts +" attempts left.", NotificationType.WARNING);
                errorTEXT.setText("Invalid 2FA code." + attempts + " attempts left.");
            } else {
                System.out.println("Invalid 2FA code. Redirecting to login page...");
                NotificationApp.showNotification("Invalid 2FA code ", "Redirecting to login page...", NotificationType.ERROR);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) twoFAfield.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        }
        SessionManager.endCode();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
