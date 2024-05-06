package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import services.StatistiqueServiceP;

import java.sql.SQLException;
import java.util.Map;

public class StatO {


    private StatistiqueServiceP statistiqueService =new StatistiqueServiceP();

    @FXML
    private PieChart pieChart;

    public StatO() {}

    @FXML
    private void initialize() {
        afficherStatistiques();
    }

    private void afficherStatistiques() {
        try {
            Map<String, Double> pourcentageParPharmacie = statistiqueService.getPourcentageOrdonnancesParPharmacie();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Ajouter les données de pourcentage par pharmacie au diagramme
            for (Map.Entry<String, Double> entry : pourcentageParPharmacie.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey() + " (" + String.format("%.2f", entry.getValue()) + "%)", entry.getValue()));
            }

            // Ajouter les données au diagramme
            pieChart.setData(pieChartData);
        } catch (SQLException e) {
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
