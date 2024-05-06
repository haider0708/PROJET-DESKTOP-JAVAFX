/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package controllers;

import javafx.scene.control.Alert;
import models.commande;
import models.livreur;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import services.commandeService;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author asus
 */

public class livreurController implements Initializable {

    int idev;


    @FXML
    private Label nomevLabel;
    @FXML
    private Label prenomevLabel;
    @FXML
    private TextField nom_clientevField;
    @FXML
    private TextField addresse_clientevField;
    @FXML
    private TextField numero_clientevField;


    @FXML
    private Label numero_telLabel;

    @FXML
    private Rating rating;

    commandeService Ps = new commandeService();
    @FXML
    private TextField idevF;
    @FXML
    private TextField iduserF;


    @FXML
    private ImageView imageview;

    @FXML
    private TextField idPartField;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {



        idevF.setVisible(false);
    }
    private final String ACCOUNT_SID = "ACf664add9b0e68f76fa70aec44731b50c";
    private final String AUTH_TOKEN = "544bf6b2b3bb1c1c5eea996d83d5ad56";
    private final String TWILIO_PHONE_NUMBER = "+18065152302";
    private livreur eve = new livreur();

    public void setlivreur(livreur e) {
        this.eve = e;

        nomevLabel.setText(e.getNom());
        prenomevLabel.setText(e.getPrenom());

        idevF.setText(String.valueOf(e.getId()));
        iduserF.setText(String.valueOf(1));
        String path = e.getImage();
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        imageview.setImage(img);


        numero_telLabel.setText(String.valueOf(e.getNumero_tel()));

    }

    public void setIdev(int idev) {
        this.idev = idev;
    }

    @FXML
    private void commanderev(MouseEvent ev) throws SQLException {
        // Récupérer les valeurs des champs
        String nomClient = nom_clientevField.getText().trim();
        String adresseClient = addresse_clientevField.getText().trim();
        String numeroClient = numero_clientevField.getText().trim();
        double ratingValue = rating.getRating();

        // Validation des champs avec retour immédiat en cas d'erreur
        if (nomClient.isEmpty()) {
            showErrorDialog("Le nom du client ne peut pas être vide.");
            return;
        } else if (nomClient.length() < 4 || nomClient.matches(".*\\d.*")) {
            showErrorDialog("Le nom doit contenir au moins 4 caractères et ne pas inclure de chiffres.");
            return;
        }

        if (adresseClient.isEmpty()) {
            showErrorDialog("L'adresse du client ne peut pas être vide.");
            return;
        } else if (adresseClient.length() < 4 || adresseClient.matches(".*\\d.*")) {
            showErrorDialog("L'adresse doit contenir au moins 4 caractères et ne pas inclure de chiffres.");
            return;
        }

        if (numeroClient.isEmpty()) {
            showErrorDialog("Le numéro de téléphone ne peut pas être vide.");
            return;
        } else if (!numeroClient.matches("\\d{8}")) {
            showErrorDialog("Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        // Si tous les contrôles sont validés, créer la commande
        commande p = new commande(
                Integer.parseInt(idevF.getText()),
                nomClient,
                adresseClient,
                numeroClient,
                ratingValue
        );

        // Ajout de la commande à la base de données
        Ps.ajoutercommande(p);

        // Envoi du SMS seulement si toutes les validations ont réussi
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber("+21622550734"),  // Numéro de téléphone du destinataire
                new PhoneNumber(TWILIO_PHONE_NUMBER),   // Numéro Twilio
                "Vous avez commandé!"
        ).create();

        System.out.println("SMS envoyé avec succès! SID: " + message.getSid());

        // Réinitialisation des champs après commande
        resetPart();
    }

    // Fonction pour afficher une boîte de dialogue d'erreur
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void resetPart() {
        nom_clientevField.setText("");
        addresse_clientevField.setText("");
        numero_clientevField.setText("");



    }




}
