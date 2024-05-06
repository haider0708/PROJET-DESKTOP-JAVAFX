package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Pharmacie;

import java.sql.Connection;

public class cardpharmaciecontroller {
    Connection cnx = null;

    private Stage stage;

    private Pharmacie pharmacie;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label nom;

    @FXML
    private Label adresse;

    @FXML
    private ImageView pharmacie_img;

    @FXML
    private Label telephone;



    public void getData(Pharmacie pharmacie) {
        this.pharmacie = pharmacie;
        nom.setText(pharmacie.getNom());
        adresse.setText(pharmacie.getAdresse());
        telephone.setText(String.valueOf(pharmacie.getNumeroTelephone()));
        Image imageprofile = new Image(pharmacie.getImg());
        pharmacie_img.setImage(imageprofile);
    }



}
