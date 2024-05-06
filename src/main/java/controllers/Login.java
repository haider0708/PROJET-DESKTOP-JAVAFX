package controllers;
import models.Patient;
import services.Service;
import services.GMailer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tray.notification.NotificationType;


public class Login implements Initializable {

    @FXML
    private Hyperlink registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Service service;

    @FXML
    private Canvas captchaCanvas;

    @FXML
    private TextField captchaField;

    private int captchaNumber;

    private GMailer emailService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.service = new Service();
        try {
            this.emailService = new GMailer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        generateCaptcha();
    }



    @FXML
    protected void handleLoginButtonAction() {
        String email = usernameField.getText();
        String password = passwordField.getText();
        String captcha = captchaField.getText();

        if (!String.valueOf(captchaNumber).equals(captcha)) {
            System.out.println("Invalid CAPTCHA. Please try again");
            NotificationApp.showNotification("Invalid CAPTCHA", "Please try again", NotificationType.NOTICE);
            generateCaptcha();
            return;
        }

        try {
            Patient loggedInUser = service.Log(email, password);

            if (loggedInUser != null) {
                System.out.println("Login successful!");
                NotificationApp.showNotification("Login successful", "SUCCESS", NotificationType.SUCCESS);
                SessionManager.startSession(loggedInUser);

                FXMLLoader loader;
                if (loggedInUser.hasRole("ROLE_ADMIN")) {
                    loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                } else {
                    Random random = new Random();
                    int twoFactorCode = 100000 + random.nextInt(900000);
                    SessionManager.saveCode(twoFactorCode);
                    emailService.sendMail("2 FACTOR AUTHENTICATOR CODE","here is your code to authenticate :"+twoFactorCode,email);
                    NotificationApp.showNotification("Two Factor Authenticator", "Check your Email to Authenticate", NotificationType.NOTICE);
                    loader = new FXMLLoader(getClass().getResource("/2FA.fxml"));
                }

                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Invalid email or password. Please try again");
                NotificationApp.showNotification("Login unsuccessful", "Invalid email or password. Please try again", NotificationType.ERROR);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    void Register() {
        try {
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
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

    @FXML
    void ForgetPassword(MouseEvent event) {
        try {
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgetPassword.fxml"));
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
    private void generateCaptcha() {
        GraphicsContext gc = captchaCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, captchaCanvas.getWidth(), captchaCanvas.getHeight());
        Random random = new Random();
        captchaNumber = random.nextInt(9999 - 1000 + 1) + 1000;
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(48));
        gc.fillText(String.valueOf(captchaNumber), captchaCanvas.getWidth() / 2.0, captchaCanvas.getHeight() / 2.0);
    }


}
