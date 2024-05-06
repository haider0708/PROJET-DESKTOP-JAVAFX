/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package controllers;

import models.livreur;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.livreurService;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class AfficherlivreurController implements Initializable {

    @FXML
    private Button stat;

    @FXML
    private GridPane gridev;

    livreurService ab=new livreurService();
    @FXML
    private TextField chercherevField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        afficherlivreur();
    }






    public void afficherlivreur(){
         try {
            List<livreur> livreur = ab.recupererlivreur();

            int row = 0;
            int column = 0;
            for (int i = 0; i < livreur.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/livreur.fxml"));
                AnchorPane pane = loader.load();
                livreurController controller = loader.getController();
                controller.setlivreur(livreur.get(i));
                controller.setIdev(livreur.get(i).getId());
                gridev.add(pane, column, row);
                column++;
                if (column > 5) {
                    column = 0;
                    row++;
                }

            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }

    @FXML
    private void rechercherlivreur(KeyEvent ev) {
        try {
            List<livreur> livreur = ab.chercherev(chercherevField.getText());
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < livreur.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/livreur.fxml"));
                AnchorPane pane = loader.load();         
                //passage de parametres
                livreurController controller = loader.getController();
                controller.setlivreur(livreur.get(i));
                controller.setIdev(livreur.get(i).getId());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }



    @FXML
    private void trierlivreur(ActionEvent ev) throws SQLException {
        try {
            List<livreur> livreur = ab.trierev();
            gridev.getChildren().clear();
            int row = 0;
            int column = 0;
            for (int i = 0; i < livreur.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/livreur.fxml"));
                AnchorPane pane = loader.load();      
                //passage de parametres
                livreurController controller = loader.getController();
                controller.setlivreur(livreur.get(i));
                controller.setIdev(livreur.get(i).getId());
                gridev.add(pane, column, row);
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
 
    }
    
}
