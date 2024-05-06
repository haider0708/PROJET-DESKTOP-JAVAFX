package controllers;
import models.Rendezvous;
import models.SmsSender;
import org.controlsfx.control.Notifications;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.Date;
import java.util.ResourceBundle;

import java.time.ZoneId;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import utils.ConnectionDB;


public class RendezvousController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tLieu;

    @FXML
    private TableColumn<Rendezvous, Date> colDate;

    @FXML
    private TableColumn<Rendezvous, String> colDescription;

    @FXML
    private TableColumn<Rendezvous, Integer> colId;

    @FXML
    private TableColumn<Rendezvous, String> colLieu;

    @FXML
    private TableView<Rendezvous> table;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private DatePicker tdate;
    int id = 0;


    @FXML
    private TextField tdescription;

    @FXML
    void clearField(ActionEvent event) {
        clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showRendezvous();
    }

    public ObservableList<Rendezvous> getRendezvous() {
        ObservableList<Rendezvous> rendezvousList = FXCollections.observableArrayList();
        String query = "SELECT * FROM rendezvous";
        con = ConnectionDB.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Rendezvous rendezvous = new Rendezvous();
                rendezvous.setId(rs.getInt("id"));
                rendezvous.setLieu(rs.getString("Lieu"));
                rendezvous.setDescription(rs.getString("description"));
                rendezvous.setDate(rs.getDate("date"));
                rendezvousList.add(rendezvous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezvousList;
    }

    public void showRendezvous() {
        ObservableList<Rendezvous> list = getRendezvous();
        table.setItems(list);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

    }


    @FXML
    void createRendezvous(ActionEvent event) {
        String lieu = tLieu.getText().trim();
        String description = tdescription.getText().trim();
        LocalDate localDate = tdate.getValue(); // Récupérer la date sélectionnée

        // Vérifier si le lieu, la description et la date sont non vides
        if (lieu.isEmpty() || description.isEmpty() || localDate == null) {
            showAlert("Champ(s) vide(s)", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si le lieu contient uniquement des caractères alphabétiques
        if (!lieu.matches("[a-zA-Z]+")) {
            showAlert("Lieu invalide", "Le lieu ne peut contenir que des lettres.");
            return;
        }

        // Vérifier si la description contient uniquement des caractères alphabétiques
        if (!description.matches("[a-zA-Z]+")) {
            showAlert("Description invalide", "La description ne peut contenir que des lettres.");
            return;
        }

        // Vérifier si la date est dans le futur ou égale à aujourd'hui
        LocalDate today = LocalDate.now();
        if (localDate.isBefore(today)) {
            showAlert("Date invalide", "Veuillez saisir une date future ou égale à aujourd'hui.");
            return;
        }

        // Vérifier si la date est dans la plage spécifiée (2024-01-01 à 2025-12-31)
        LocalDate minDate = LocalDate.of(2024, 4, 22);
        LocalDate maxDate = LocalDate.of(2025, 4, 22);
        if (localDate.isBefore(minDate) || localDate.isAfter(maxDate)) {
            showAlert("Date invalide", "La date doit être comprise entre 2024-04-22 et 2025-04-22.");
            return;
        }

        // Vérifier si un rendez-vous existe déjà pour la date sélectionnée
        if (isRendezvousExistsForDate(localDate)) {
            showAlert("Date invalide", "Un rendez-vous existe déjà pour cette date. Veuillez choisir une autre date.");
            return;
        }

        // Si toutes les vérifications passent, insérer dans la base de données
        String insert = "insert into rendezvous(Lieu,description,date) values(?,?,?)";
        con = ConnectionDB.getCon();
        try {
            st = con.prepareStatement(insert);
            st.setString(1, lieu);
            st.setString(2, description);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            st.setDate(3, sqlDate);
            st.executeUpdate();
            clear();
            showRendezvous();
            showSuccessAlert("Rendez-vous enregistré", "Le rendez-vous a été enregistré avec succès.");
            // Affichage de la notification de confirmation
            Notifications.create()
                    .title("Confirmation de rendez-vous")
                    .text("Le rendez-vous a été enregistré avec succès.")
                    .showInformation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour vérifier si un rendez-vous existe déjà pour une date donnée
    private boolean isRendezvousExistsForDate(LocalDate date) {
        String query = "SELECT COUNT(*) FROM rendezvous WHERE date = ?";
        try {
            st = con.prepareStatement(query);
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            st.setDate(1, sqlDate);
            rs = st.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void getData(MouseEvent event) {
        Rendezvous rendezvous = table.getSelectionModel().getSelectedItem();
        id = rendezvous.getId();
        tLieu.setText(rendezvous.getLieu());
        tdescription.setText(rendezvous.getDescription());
        // Convertir java.util.Date en LocalDate
        LocalDate localDate = rendezvous.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        tdate.setValue(localDate);
        btnSave.setDisable(true);
    }


    void clear() {
        tLieu.setText(null);
        tdescription.setText(null);
        tdate.setValue(null);
        btnSave.setDisable(false);
    }


    @FXML
    void deleteRendezvous(ActionEvent event) {
        String delete = "delete from rendezvous where id = ?";
        con = ConnectionDB.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1, id);
            st.executeUpdate();
            showRendezvous();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateRendezvous(ActionEvent event) {
        String lieu = tLieu.getText().trim();
        String description = tdescription.getText().trim();
        LocalDate localDate = tdate.getValue(); // Récupérer la date sélectionnée

        // Vérifier si le lieu, la description et la date sont non vides
        if (lieu.isEmpty() || description.isEmpty() || localDate == null) {
            showAlert("Champ(s) vide(s)", "Veuillez remplir tous les champs.");
            return;
        }

        // Convertir la date en une chaîne au format YYYY-MM-DD
        String dateStr = localDate.toString();

        // Vérifier si le lieu contient uniquement des caractères alphabétiques
        if (!lieu.matches("[a-zA-Z]+")) {
            showAlert("Lieu invalide", "Le lieu ne peut contenir que des lettres.");
            return;
        }

        // Vérifier si la description contient uniquement des caractères alphabétiques
        if (!description.matches("[a-zA-Z]+")) {
            showAlert("Description invalide", "La description ne peut contenir que des lettres.");
            return;
        }

        // Vérifier si la date est dans le futur ou égale à aujourd'hui
        LocalDate today = LocalDate.now();
        if (localDate.isBefore(today)) {
            showAlert("Date invalide", "Veuillez saisir une date future ou égale à aujourd'hui.");
            return;
        }

        // Vérifier si la date est dans la plage spécifiée (2024-01-01 à 2025-12-31)
        LocalDate minDate = LocalDate.of(2024, 4, 22);
        LocalDate maxDate = LocalDate.of(2025, 4, 22);
        if (localDate.isBefore(minDate) || localDate.isAfter(maxDate)) {
            showAlert("Date invalide", "La date doit être comprise entre 2024-04-22 et 2025-04-22.");
            return;
        }

        // Si toutes les vérifications passent, mettre à jour dans la base de données
        String update = "update rendezvous set Lieu = ?, description = ?, date = ? where id =?";
        con = ConnectionDB.getCon();
        try {
            st = con.prepareStatement(update);
            st.setString(1, lieu);
            st.setString(2, description);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            st.setDate(3, sqlDate);
            st.setInt(4, id);
            st.executeUpdate();
            showRendezvous();
            clear();
            showSuccessAlert("Rendez-vous mis à jour", "Le rendez-vous a été mis à jour avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnsms(ActionEvent event) {
        Rendezvous rendezvous = table.getSelectionModel().getSelectedItem();
        if (rendezvous != null) {
            String toPhoneNumber = "+216" + "55417163";
            System.out.println(toPhoneNumber);
            SmsSender.sendVerificationCode(toPhoneNumber, rendezvous.getLieu());
        } else {
            // Gérer le cas où aucun rendez-vous n'est sélectionné
            showAlert("Rendez-vous non sélectionné", "Veuillez sélectionner un rendez-vous.");
        }
    }


    public void handleSendButtonAction(ActionEvent actionEvent) {


        // Récupérer le texte saisi dans le champ de texte
        String userInput = inputField.getText();

        // Générer une réponse en fonction de l'entrée de l'utilisateur
        String response = getResponse(userInput);

        // Afficher la saisie de l'utilisateur et la réponse du chatbot dans la zone de chat
        chatArea.appendText("You: " + userInput + "\n");
        chatArea.appendText("ChatBot: " + response + "\n");

        // Effacer le champ de texte après l'envoi du message
        inputField.clear();
    }

    // Méthode pour générer une réponse en fonction de l'entrée de l'utilisateur
    private String getResponse(String input) {
        // Mettre en minuscules pour une correspondance insensible à la casse
        input = input.toLowerCase();

        // Logique pour répondre à différentes entrées de l'utilisateur
        switch (input) {
            case "hello":
            case "hi":
                return "Hello there!";
            case "how are you?":
                return "I'm just a chatbot, but thanks for asking!";

            case "comment se déroule la consultation en ligne ?":
                return "The online consultation is conducted via secure video conference.";

            case "quels sont les médecins disponibles pour une consultation en ligne ?":
                return "Currently, our available doctors for online consultations are Dr. Smith and Dr. Dupont.";

            case "comment puis-je payer pour la consultation en ligne ?":
                return "Once your appointment is confirmed, you will receive a payment link.";

            case "comment puis-je annuler ou reporter mon rendez-vous en ligne ?":
                return "You can cancel or reschedule your online appointment by contacting us directly by phone or email.";

            case "mes informations médicales seront-elles sécurisées ?":
                return "Absolutely. We use advanced technologies to protect your confidentiality.";
            case "bye":
                return "Goodbye!";
            default:
                return "I'm sorry, I didn't understand that.";
        }
    }




}