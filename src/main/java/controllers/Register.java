package controllers;

import models.Patient;
import services.Service;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.UUID;
import org.mindrot.jbcrypt.*;
import tray.notification.NotificationType;

public class Register {

    @FXML
    private Text AddressERR;

    @FXML
    private Text EmailERR;

    @FXML
    private Text FirstnameERR;

    @FXML
    private Text NumberERR;

    @FXML
    private Text PasswordERR;

    @FXML
    private Text ageERR;

    @FXML
    private Text lastnameERR;

    @FXML
    private Text sexeERR;

    @FXML
    private TextField addressField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField numberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<?> sexeChoiceBox;

    @FXML
    private Hyperlink loginbutton;

    boolean isValid = true;
    private Service service ;

    public Register() {
        this.service = new Service();
    }

    @FXML
    void chooseFileButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(((Stage) imageField.getScene().getWindow()));

        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }

    }

    @FXML
    void Login() {
        try {
            Stage currentStage = (Stage) loginbutton.getScene().getWindow();
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

    @FXML
    void Register(MouseEvent event) {
        String email = emailField.getText();
        String role = "ROLE_USER";
        String password = passwordField.getText();
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String sexe = (String) sexeChoiceBox.getValue();
        String ageText = ageField.getText();
        String number = numberField.getText();
        String img_path = imageField.getText();
        String address = addressField.getText();
        if (validateFields(email, password, firstname, lastname, sexe, ageText, number, address)) {
            try {
                String hashedPassword = hashPassword(password);
                File selectedFile = new File(img_path);
                Path sourcePath = selectedFile.toPath();
                String fileName = generateUniqueFileName(selectedFile.getName());
                Path targetPath = new File("src/main/java/Images/" + fileName).toPath();
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                String imagePath = targetPath.toAbsolutePath().toString();
                addPatient(email, role, hashedPassword, firstname, lastname, sexe, ageText, number, imagePath, address);
                NotificationApp.showNotification("Success", "Patient Added", NotificationType.SUCCESS);
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
    }


    private void addPatient(String email, String role, String password, String firstname, String lastname, String sexe, String ageText, String number, String img_path, String address) {
        try {
            int age = Integer.parseInt(ageText);
            Gson gson = new Gson();
            String rolesJson = gson.toJson(new String[]{role});
            Patient newPatient = new Patient(0, email, rolesJson, password, firstname, lastname, sexe, age, number, img_path, address, false, null);
            Service.ajouter(newPatient);
            System.out.println("Patient added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSymbol(ch)) {
                hasSymbol = true;
            }
        }
        return hasLower && hasUpper && hasDigit && hasSymbol;
    }

    private boolean isSymbol(char ch) {
        String symbols = "!@#$%^&*()-_+=[]{}|;:,.<>?";
        return symbols.indexOf(ch) != -1;
    }

    private boolean validateName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        return name.length() >= 4 && name.matches("[a-zA-Z]+");
    }



    private String generateUniqueFileName(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = fileName.substring(dotIndex);
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        return uniqueFileName;
    }

    private String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }

    private boolean validateFields(String email,String password, String firstname, String lastname, String sexe, String ageText, String number , String address ) {


        if (email.isEmpty())  {
            EmailERR.setText("Please enter an email address.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            EmailERR.setText("Please enter a valid email address.");
            isValid = false;
        } else {
            try {
                if (service.emailExists(email)) {
                    EmailERR.setText("This email exist.");
                    isValid = false;
                } else {
                    EmailERR.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                isValid = false;
            }
        }
        if (password.isEmpty()) {
            PasswordERR.setText("Please enter a password.");
            isValid = false;
        } else if (!validatePassword(password)) {
            PasswordERR.setText("Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one symbol.");
            isValid = false;
        } else {
            PasswordERR.setText("");
        }

        if (!firstname.isEmpty()) {
            if (!validateName(firstname)) {
                FirstnameERR.setText("First name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                FirstnameERR.setText("");
            }
        } else {
            FirstnameERR.setText("Please provide your first name.");
            isValid = false;
        }

        if (!lastname.isEmpty()) {
            if (!validateName(lastname)) {
                lastnameERR.setText("Last name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                lastnameERR.setText("");
            }
        } else {
            lastnameERR.setText("Please provide your Last name.");
            isValid = false;
        }

        if (sexe == null) {
            sexeERR.setText("Please select a gender.");
            isValid = false;
        } else {
            sexeERR.setText("");
        }

        if (!ageText.isEmpty()) {
            try {
                int age = Integer.parseInt(ageText);
                if (age < 10 || age > 99) { // Ensure age is between 10 and 99
                    ageERR.setText("Age must be exactly two digits.");
                    isValid = false;
                } else {
                    ageERR.setText("");
                }
            } catch (NumberFormatException e) {
                ageERR.setText("Please enter a valid age.");
                isValid = false;
            }
        } else {
            ageERR.setText("Please enter your age.");
            isValid = false;
        }

        if (!number.isEmpty()) {
            if (!number.matches("\\d{8}")) { // Check if number contains exactly 8 digits
                NumberERR.setText("Phone number must be exactly 8 digits.");
                isValid = false;
            } else {
                NumberERR.setText("");
            }
        } else {
            NumberERR.setText("Please enter a phone number.");
            isValid = false;
        }

        if (address.isEmpty()) {
            AddressERR.setText("Please enter an address.");
            isValid = false;
        } else {
            AddressERR.setText("");
        }
        return isValid;


    }


}
