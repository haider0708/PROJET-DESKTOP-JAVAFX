package controllers;

import models.Patient;
import services.Service;
import services.InfobipService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.control.Hyperlink;
import tray.notification.NotificationType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Random;


public class ForgetPassword implements Initializable {
    private Service Service;

    private InfobipService Sms;

    @FXML
    private Button Code;

    @FXML
    private TextField CodeField;

    @FXML
    private TextField ConfirmPassword;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField NewPassword;

    @FXML
    private Button Search;

    @FXML
    private Button confirm;

    @FXML
    private ImageView emailimage;

    @FXML
    private ImageView passwordimage1;

    @FXML
    private ImageView passwordimage2;

    @FXML
    private ImageView codeimage;

    @FXML
    private Hyperlink login;

    @FXML
    void code(ActionEvent event) {
        String enteredCode = CodeField.getText();
        if (enteredCode.equals(SessionManager.getCodeS())) {
            ConfirmPassword.setVisible(true);
            NewPassword.setVisible(true);
            confirm.setVisible(true);
            passwordimage1.setVisible(true);
            passwordimage2.setVisible(true);
            NotificationApp.showNotification("Success", "Code is correct.input your new password", NotificationType.SUCCESS);
        } else {
            NotificationApp.showNotification("INCORRECT CODE", "Please try again", NotificationType.WARNING);
            System.out.println("Incorrect code. Please try again");
        }
    }

    @FXML
    void confirm(ActionEvent event) throws SQLException, IOException {
        String newPassword = NewPassword.getText();
        String confirmPassword = ConfirmPassword.getText();
        if (newPassword.equals(confirmPassword)) {
            String hashedPassword = hashPassword(newPassword);
            SessionManager.getCurrentSession().setPassword(hashedPassword);
            Service.update(SessionManager.getCurrentSession());
            NotificationApp.showNotification("Success", "Password updated", NotificationType.SUCCESS);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) confirm.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Passwords do not match. Please try again");
            NotificationApp.showNotification("Passwords do not match", "Please try again", NotificationType.WARNING);
        }
    }

    @FXML
    void search(ActionEvent event) throws SQLException, IOException {
        String email = EmailField.getText();
        Patient patient = Service.getPatientByEmail(email);
        if (patient != null) {
            SessionManager.startSession(patient);
            Code.setVisible(true);
            CodeField.setVisible(true);
            codeimage.setVisible(true);
            String code = generateCode();
            SessionManager.saveCodeS(code);
            Sms.sendSMS("216"+patient.getNumber(), code);
            NotificationApp.showNotification("SMS", "Check your phone for your code", NotificationType.SUCCESS);

        } else {
            System.out.println("No user found with this email");
            NotificationApp.showNotification("Missmatch", "No user found with this email", NotificationType.WARNING);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.Service = new Service();
        this.Sms = new InfobipService();
        Code.setVisible(false);
        CodeField.setVisible(false);
        ConfirmPassword.setVisible(false);
        NewPassword.setVisible(false);
        confirm.setVisible(false);
        passwordimage1.setVisible(false);
        passwordimage2.setVisible(false);
        codeimage.setVisible(false);
    }



        public static String generateCode() {
            Random random = new Random();
            int code = random.nextInt(900000) + 100000;
            return String.valueOf(code);
        }

    private String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }
    @FXML
    void login(MouseEvent event) {
        try {
            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage registerStage = new Stage();
            registerStage.setTitle("Register");
            Scene scene = new Scene(root);
            registerStage.setScene(scene);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
