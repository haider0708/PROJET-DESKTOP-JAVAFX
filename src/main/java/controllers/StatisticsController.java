package controllers;

import models.livreur;
import services.livreurService;
import services.commandeService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatisticsController implements Initializable {

    @FXML
    private ImageView GoBackBtn;
    @FXML
    private PieChart StatsChart;

    livreurService rs = new livreurService();
    commandeService cs = new commandeService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            displayStatistics();
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayStatistics() throws SQLException {
        List<livreur> livreurs = rs.recupererlivreur();

        // Compter le nombre de commandes par livreur
        Map<Integer, Integer> commandesParLivreur = cs.countCommandesParLivreur();

        // Créer une liste de données pour le PieChart
        List<PieChart.Data> pieChartData = livreurs.stream()
                .map(livreur -> new PieChart.Data(livreur.getNom() + " (" + commandesParLivreur.getOrDefault(livreur.getId(), 0) + " commandes)", commandesParLivreur.getOrDefault(livreur.getId(), 0)))
                .collect(Collectors.toList());

        // Afficher les données dans le PieChart
        StatsChart.setData(FXCollections.observableArrayList(pieChartData));

        // Ajouter des fonctionnalités d'interactivité pour afficher des informations supplémentaires lors du clic sur les données
        StatsChart.getData().forEach(data -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                String nom = data.getName().split(" ")[0];
                int idLivreur = Integer.parseInt(nom);
                int nombreCommandes = commandesParLivreur.getOrDefault(idLivreur, 0);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Statistiques par livreur");
                alert.setHeaderText("Livreur ID : " + idLivreur);
                alert.setContentText("Nombre de commandes : " + nombreCommandes);
                alert.showAndWait();
            });
        });
    }
}
