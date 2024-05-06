package controllers;




import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Pharmacie;
import services.ServicePharmacie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class frontPharmacie{
    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchField;

    private final ServicePharmacie ps = new ServicePharmacie();

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

        List<Pharmacie> pharmacies = ps.selectAll();
        if (pharmacies.isEmpty()) {
            System.out.println("pharmacies est vide.");
            return;
        }

        int column = 0;
        int row = 3;

        grid.setHgap(50);

        for (Pharmacie pharmacie : pharmacies) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cardfront.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                cardpharmaciecontroller p = fxmlLoader.getController();
                if (p == null) {
                    System.out.println("Le contrôleur de l'élément n'a pas été initialisé.");
                    continue;
                }

                p.getData(pharmacie);



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




}