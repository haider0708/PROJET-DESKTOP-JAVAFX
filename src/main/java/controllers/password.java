package controllers;

import models.Patient;
import services.Service;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.mindrot.jbcrypt.BCrypt;
import tray.notification.NotificationType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class password implements Initializable {
    @FXML
    private TextField confirmpass;

    @FXML
    private TextField newpass;

    @FXML
    private TextField oldpass;

    @FXML
    private Text passError;

    @FXML
    void cancel(MouseEvent event) {

    }

    @FXML
    void save(MouseEvent event) throws SQLException {
        Patient currentPatient = SessionManager.getCurrentSession();
        if (currentPatient != null) {
            String oldPassword = oldpass.getText();
            String newPassword = newpass.getText();
            String confirmPassword = confirmpass.getText();
            if (BCrypt.checkpw(oldPassword, currentPatient.getPassword())) {
                if (newPassword.equals(confirmPassword)) {
                    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    currentPatient.setPassword(hashedPassword);
                    Service service = new Service();
                    service.update(currentPatient);
                    NotificationApp.showNotification("Success", "Password updated", NotificationType.SUCCESS);
                } else {
                    passError.setText("New password and confirm password do not match.");
                }
            } else {
                passError.setText("Old password is incorrect.");
            }
        }
        oldpass.setText(null);
        newpass.setText(null);
        confirmpass.setText(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
