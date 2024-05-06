package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import models.Produit;

import java.io.IOException;
import java.util.List;

public class PanierController {
    @FXML
    private TableColumn<Produit, String> nomProduitColumn;

    @FXML
    private TableColumn<Produit, String> prixProduitColumn;
    @FXML
    private TableView<Produit> panierTable;

    // Méthode pour récupérer le produit sélectionné dans la table panierTable
    public Produit getProduitSelectionne() {
        return panierTable.getSelectionModel().getSelectedItem();
    }
    public void initialize() {
        // Initialiser les colonnes avec les propriétés de Produit
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prixProduitColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
    }

    // Cette méthode charge les produits dans la TableView
    public void chargerProduitsDansPanier(List<Produit> produits) {
        panierTable.getItems().addAll(produits);
    }

    @FXML
    private void ouvrirListeProduits(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frontProduit.fxml"));
            Parent root = loader.load();

            // Obtenir une instance du contrôleur frontProduit
            frontProduit frontController = loader.getController();

            // Récupérer le produit sélectionné dans la table panierTable
            Produit selectedProduit = getProduitSelectionne();

            // Vérifier si un produit est sélectionné avant d'appeler setProduitInfo
            if (selectedProduit != null) {
                // Appeler la méthode setProduitInfo du contrôleur frontProduit
                frontController.setProduitInfo(selectedProduit.getNom(), selectedProduit.getPrix());
            }

            // Afficher la nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Produits");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


