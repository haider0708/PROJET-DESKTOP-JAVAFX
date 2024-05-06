package controllers;

import models.Patient;
import services.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import tray.notification.NotificationType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

public class EditProfil implements Initializable {

    private Service Service;
    @FXML
    private Text addressERR;

    @FXML
    private TextField addressField;

    @FXML
    private Text ageERR;

    @FXML
    private TextField ageFiled;

    @FXML
    private Text emailERR;

    @FXML
    private TextField emailField;

    @FXML
    private Text firstnameERR;

    @FXML
    private TextField firstnameField;

    @FXML
    private ImageView image;

    @FXML
    private Text lastNameERR;

    @FXML
    private TextField lastnameField;

    @FXML
    private Text numberERR;

    @FXML
    private TextField numberField;

    @FXML
    void Cancel(MouseEvent event) {

    }

    public EditProfil() {
        this.Service = new Service();
    }

    @FXML
    void Save(MouseEvent event) {
        try {
            String email = emailField.getText();
            String firstname = firstnameField.getText();
            String lastname = lastnameField.getText();
            String ageText = ageFiled.getText();
            String number = numberField.getText();
            String address = addressField.getText();
            boolean isValid = validateFields(email, firstname, lastname, ageText, number, address);

            if (isValid) {
                Patient currentPatient = SessionManager.getCurrentSession();
                if (currentPatient != null) {
                    NotificationApp.showNotification("UPDATED", "Patient updated successfully", NotificationType.SUCCESS);
                    currentPatient.setEmail(email);
                    currentPatient.setFirstname(firstname);
                    currentPatient.setLastname(lastname);
                    currentPatient.setAge(Integer.parseInt(ageText));
                    currentPatient.setNumber(number);
                    currentPatient.setAddress(address);
                    Service.update(currentPatient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during update: " + e.getMessage());
        }

    }

    @FXML
    void upload(MouseEvent event) throws SQLException, FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Path sourcePath = selectedFile.toPath();
            String fileName = generateUniqueFileName(selectedFile.getName());
            Path targetPath = new File("src/main/java/Images/" + fileName).toPath();
            try {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error during file copy: " + e.getMessage());
            }
            String imagePath = targetPath.toAbsolutePath().toString();
            Patient currentUser = SessionManager.getCurrentSession();

            if (currentUser != null) {
                currentUser.setImg_path(imagePath);
                Service.update(currentUser);
            }
            Image newImage = new Image(new FileInputStream(imagePath));
            image.setImage(newImage);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Patient currentPatient = SessionManager.getCurrentSession();
        if (currentPatient != null) {
            addressField.setText(currentPatient.getAddress());
            ageFiled.setText(String.valueOf(currentPatient.getAge()));
            emailField.setText(currentPatient.getEmail());
            firstnameField.setText(currentPatient.getFirstname());
            lastnameField.setText(currentPatient.getLastname());
            numberField.setText(currentPatient.getNumber());
        }

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

    private boolean validateFields(String email, String firstname, String lastname, String ageText, String number, String address) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailERR.setText("Please enter an email address.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailERR.setText("Please enter a valid email address.");
            isValid = false;
        }
        if (!firstname.isEmpty()) {
            if (!validateName(firstname)) {
                firstnameERR.setText("First name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                firstnameERR.setText("");
            }
        }
        if (!lastname.isEmpty()) {
            if (!validateName(lastname)) {
                lastNameERR.setText("Last name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                lastNameERR.setText("");
            }
        }
        int age = 0;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            isValid = false;
        }
        if (age <= 0 && number.isEmpty()) {
            ageERR.setText("Please enter your age and phone number.");
            isValid = false;
        } else {
            if (age <= 0) {
                ageERR.setText("Please enter a valid age.");
                isValid = false;
            } else if (age < 10 || age > 99) {
                ageERR.setText("Age must be between 10 and 99.");
                isValid = false;
            } else {
                ageERR.setText("");
            }
        }
        if (number.isEmpty()) {
            numberERR.setText("Please enter a phone number.");
            isValid = false;
        } else if (!number.matches("\\d{8}")) {
            numberERR.setText("Phone number must be exactly 8 digits.");
            isValid = false;
        } else {
            numberERR.setText("");
        }


        if (address.isEmpty()) {
            addressERR.setText("Please enter an address.");
            isValid = false;
        } else {
            addressERR.setText("");
        }

        return isValid;
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private boolean validateName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        return name.length() >= 4 && name.matches("[a-zA-Z]+");
    }
}
