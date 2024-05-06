package controllers;







import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Produit;
import services.ServiceProduit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class frontProduit {

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchField;
    @FXML
    private Label nomProduitLabel;

    @FXML
    private Label prixProduitLabel;


    private final ServiceProduit ps = new ServiceProduit();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    void initialize() {
        try {
            actualise();
            // Ajouter un écouteur d'événements au champ de recherche
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (newValue.isEmpty()) {
                        // Si le champ de recherche est vide, afficher tous les articles
                        actualise();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualise() throws SQLException {
        // Effacer les éléments actuels de la grille
        grid.getChildren().clear();

        List<Produit> produits = ps.selectAll();
        if (produits.isEmpty()) {
            System.out.println("produit est vide.");
            return;
        }

        int column = 0;
        int row = 3;

        grid.setHgap(50);

        for (Produit produit : produits) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cardfrontproduit.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                cardproduitcontroller p = fxmlLoader.getController();
                if (p == null) {
                    System.out.println("Le contrôleur de l'élément n'a pas été initialisé.");
                    continue;
                }

                p.getData(produit);



                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void afficherProduitSelectionne(Produit produit) {
        nomProduitLabel.setText(produit.getNom());
        prixProduitLabel.setText(String.valueOf(produit.getPrix()));
    }
    @FXML
    private void ouvrirListePaniers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Panier.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Produits du Panier");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setProduitInfo(String nom, float prix) {
    }
}