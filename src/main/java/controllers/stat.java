package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import services.StatistiqueService;

import java.util.Map;

public class stat{

    private StatistiqueService statistiqueService = new StatistiqueService();

    @FXML
    private PieChart pieChart;

    @FXML
    private void initialize() {
        afficherStatistiques();
    }

    private void afficherStatistiques() {
        try {
            // Récupérer les statistiques par catégorie de produit
            Map<String, Integer> statistiquesParCategorie = statistiqueService.getNombreProduitsParCategorie();

            // Créer une liste observable pour les données du diagramme
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            int totalProduits = 0;

            // Calculer le nombre total de produits
            for (Integer value : statistiquesParCategorie.values()) {
                totalProduits += value;
            }

            // Ajouter les données et les pourcentages au diagramme
            for (Map.Entry<String, Integer> entry : statistiquesParCategorie.entrySet()) {
                double pourcentage = (double) entry.getValue() / totalProduits * 100;
                pieChartData.add(new PieChart.Data(entry.getKey() + " (" + String.format("%.2f", pourcentage) + "%)", entry.getValue()));
            }

            // Ajouter les données au diagramme
            pieChart.setData(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertWithError("Erreur lors de la récupération des statistiques", e.getMessage());
        }
    }

    private void showAlertWithError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
