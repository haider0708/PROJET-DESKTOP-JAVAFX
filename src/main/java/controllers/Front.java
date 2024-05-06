package controllers;

import models.Patient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Front implements Initializable {

    @FXML
    private Pane Pane;

    @FXML
    private ImageView userimage;

    @FXML
    private Text username;

    @FXML
    void Setting(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Pane addUserView = loader.load();
            Pane.getChildren().setAll(addUserView);
            Pane.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void Livraison(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontLivreur.fxml"));
            Pane addUserView = loader.load();
            Pane.getChildren().setAll(addUserView);
            Pane.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void Produit(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontProduitFXML.fxml"));
            Pane addUserView = loader.load();
            Pane.getChildren().setAll(addUserView);
            Pane.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Pharmacie(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontPharmacieFXML.fxml"));
            Pane addUserView = loader.load();
            Pane.getChildren().setAll(addUserView);
            Pane.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @FXML
    void Rendezvous(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Rendezvous.fxml"));
            Pane addUserView = loader.load();
            Pane.getChildren().setAll(addUserView);
            Pane.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @FXML
    void event(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client.fxml"));
            Pane addUserView = loader.load();
            Pane.getChildren().setAll(addUserView);
            Pane.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Patient currentPatient = SessionManager.getCurrentSession();
        if (currentPatient != null) {
            String imagePath = currentPatient.getImg_path();
            Image image = new Image(imagePath);
            userimage.setImage(image);
            String fullName = currentPatient.getFirstname() + " " + currentPatient.getLastname();
            username.setText(fullName);
        }

    }

}
