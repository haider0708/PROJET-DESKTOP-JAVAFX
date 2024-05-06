package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.util.converter.NumberStringConverter;
import models.Rapport;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.chart.PieChart;
import utils.ConnectionDB;


public class RapportController implements Initializable {
    Connection con = null;
    PreparedStatement st1 = null;
    ResultSet rs1 = null;

    @FXML
    private PieChart pieChart;
    @FXML
    private Button btnDelete2;


    @FXML
    private Button btnSave2;

    @FXML
    private Button btnUpdate2;

    @FXML
    private TextField tNote;


    @FXML
    private ChoiceBox<String> tType1;

    @FXML
    private ChoiceBox<Integer> tRdvId;

    @FXML
    private TextField tType2;


    @FXML
    private Button btnSearch;

    @FXML
    private TextField textFieldSearch;
    @FXML
    private TableColumn<Rapport, String> colNote;

    @FXML
    private TableColumn<Rapport, String> colType;

    @FXML
    private TableColumn<Rapport, Integer> colRdvId;

    @FXML
    private TableView<Rapport> table2;


    @FXML
    private Label lblTotalPatients;

    @FXML
    private Label analysesLabel;

    @FXML
    private Label consultationsLabel;

    @FXML
    private Label diabetiquesLabel;

    @FXML
    private VBox statisticsBox;

    @FXML
    private Label totalPatientsLabel;


    @FXML
    private Button tPdf;


    @FXML
    private Button btnStatistics;

    @FXML
    public void exportToPDF(ActionEvent event) {
        generatePDF(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = ConnectionDB.getCon();
        showRapports();

        loadRdvIdsFromDatabase();

        tType1.setItems(FXCollections.observableArrayList(
                "resultat_analyses",
                "consultation_psychologique",
                "diabetique"
        ));

        tType1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tType2.setText(newValue);
        });


        // Tri par la colonne "Note" dans l'ordre alphabétique croissant
        colNote.setSortType(TableColumn.SortType.ASCENDING);
        table2.getSortOrder().setAll(colNote); // Ajouter la colonne de tri
        table2.sort();

// Tri par la colonne "Type" dans l'ordre alphabétique croissant
        colType.setSortType(TableColumn.SortType.ASCENDING);
        table2.getSortOrder().setAll(colType); // Ajouter la colonne de tri
        table2.sort();

// Tri par la colonne "Rdv ID" dans l'ordre numérique croissant
        colRdvId.setSortType(TableColumn.SortType.ASCENDING);
        table2.getSortOrder().setAll(colRdvId); // Ajouter la colonne de tri
        table2.sort();
        // Appel de la fonction afficherRepartitionRapport() pour afficher les statistiques
        afficherRepartitionRapport();
    }

    private void loadRdvIdsFromDatabase() {
        ObservableList<Integer> rdvIdList = FXCollections.observableArrayList();
        String query = "SELECT ID FROM rendezvous";
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int rdvId = rs.getInt("ID");
                rdvIdList.add(rdvId);
            }
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de la récupération des identifiants de rendez-vous.", e);
        }
        tRdvId.setItems(rdvIdList);
    }

    @FXML
    public void CreateRapport(ActionEvent actionEvent) {
        String note = tNote.getText().trim();
        String type = tType1.getValue(); // Récupérer la valeur sélectionnée dans tType1

        // Validation de la saisie
        if (!validateInput(note, type)) {
            return;
        }

        // Insérer dans la base de données
        String insert = "INSERT INTO rapport(Note, Type, rdv_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(insert);
            st.setString(1, note);
            st.setString(2, type);
            st.setInt(3, tRdvId.getValue()); // Récupérer la valeur sélectionnée dans tRdvId
            st.executeUpdate();
            clear();
            showSuccessAlert("Rapport enregistré", "Le rapport a été enregistré avec succès.");
            // Actualiser l'affichage après l'ajout
            showRapports();
            // Mettre à jour les statistiques
            afficherRepartitionRapport();
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de l'insertion du rapport.", e);
        }
    }

    private boolean validateInput(String note, String type) {
        if (!note.matches("[a-zA-Z ]+")) {
            showAlert("Format de note invalide", "La note ne doit contenir que des caractères alphabétiques.");
            return false;
        }
        if (!isValidType(type)) {
            showAlert("Type invalide", "Le type doit être l'un des choix proposés.");
            return false;
        }
        return true;
    }

    private boolean isValidType(String type) {
        return type.equals("resultat_analyses") ||
                type.equals("consultation_psychologique") ||
                type.equals("diabetique");
    }

    void showRapports() {
        ObservableList<Rapport> rapportList = FXCollections.observableArrayList();
        String query = "SELECT rapport.*, rendezvous.ID AS rdvId FROM rapport " +
                "JOIN rendezvous ON rapport.rdv_id = rendezvous.ID";
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String note = rs.getString("Note");
                String type = rs.getString("Type");
                int rdvId = rs.getInt("rdvId");
                Rapport rapport = new Rapport(note, type, rdvId);
                rapportList.add(rapport);
            }
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de la récupération des rapports.", e);
        }
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRdvId.setCellValueFactory(new PropertyValueFactory<>("rdvId"));
        table2.setItems(rapportList);
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void handleException(String title, String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message + "\n" + e.getMessage());
        alert.showAndWait();
    }

    void clear() {
        tNote.setText(null);
        tType1.setValue(null);
        tType2.setText(null);
        tRdvId.setValue(null);
    }

    public void UpdateRapport(ActionEvent actionEvent) {
        // Récupérer le rapport sélectionné dans la table
        Rapport selectedRapport = table2.getSelectionModel().getSelectedItem();
        if (selectedRapport == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un rapport à mettre à jour.");
            return;
        }

        // Récupérer la valeur du type à partir du ChoiceBox tType1
        String type = tType1.getValue();
        if (type == null) {
            showAlert("Type manquant", "Veuillez sélectionner un type pour le rapport.");
            return;
        }

        // Récupérer les valeurs des autres champs
        String note = tNote.getText().trim();
        Integer rdvId = tRdvId.getValue(); // Utiliser Integer pour accepter null

        // Vérifier si la note contient uniquement des caractères alphabétiques
        if (!note.matches("[a-zA-Z]+")) {
            showAlert("Note invalide", "La note ne doit contenir que des caractères alphabétiques.");
            return;
        }

        // Vérifier si rdvId est null avant d'appeler intValue()
        int rdvIdInt = rdvId != null ? rdvId.intValue() : 0;

        // Mettre à jour le rapport dans la base de données
        String updateQuery = "UPDATE rapport SET Note=?, Type=?, rdv_id=? WHERE rdv_id=?";
        try {
            PreparedStatement st = con.prepareStatement(updateQuery);
            st.setString(1, note);
            st.setString(2, type);
            st.setInt(3, rdvIdInt);
            st.setInt(4, selectedRapport.getRdvId());
            st.executeUpdate();
            showSuccessAlert("Rapport mis à jour", "Le rapport a été mis à jour avec succès.");

            // Actualiser l'affichage après la mise à jour dans la base de données
            showRapports();
            // Mettre à jour les statistiques
            afficherRepartitionRapport();
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de la mise à jour du rapport.", e);
        }
    }


    public void DeleteRapport(ActionEvent actionEvent) {
        Rapport selectedRapport = table2.getSelectionModel().getSelectedItem();
        if (selectedRapport != null) {
            // Supprimer le rapport de la base de données en utilisant rdvId
            String deleteQuery = "DELETE FROM rapport WHERE rdv_id=?";
            try {
                PreparedStatement st = con.prepareStatement(deleteQuery);
                st.setInt(1, selectedRapport.getRdvId());
                st.executeUpdate();
                clear();
                showSuccessAlert("Rapport supprimé", "Le rapport a été supprimé avec succès.");
                // Actualiser l'affichage après la suppression
                showRapports();
                // Mettre à jour les statistiques
                afficherRepartitionRapport();
            } catch (SQLException e) {
                handleException("Erreur SQL", "Une erreur s'est produite lors de la suppression du rapport.", e);
            }
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un rapport à supprimer.");
        }
    }

    @FXML
    void searchRapports(ActionEvent event) {
        String keyword = textFieldSearch.getText().trim(); // Récupérer le mot-clé de recherche depuis un champ de texte
        ObservableList<Rapport> rapportList = searchRapportsFromDatabase(keyword);
        // Mettre à jour la table avec les rapports trouvés
        table2.setItems(rapportList);
    }

    private ObservableList<Rapport> searchRapportsFromDatabase(String keyword) {
        ObservableList<Rapport> rapportList = FXCollections.observableArrayList();
        String query = "SELECT rapport.*, rendezvous.ID AS rdvId FROM rapport " +
                "JOIN rendezvous ON rapport.rdv_id = rendezvous.ID " +
                "WHERE Note LIKE ? OR Type LIKE ? OR rdv_id LIKE ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            for (int i = 1; i <= 3; i++) {
                st.setString(i, "%" + keyword + "%");
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String note = rs.getString("Note");
                String type = rs.getString("Type");
                int rdvId = rs.getInt("rdvId");
                Rapport rapport = new Rapport(note, type, rdvId);
                rapportList.add(rapport);
            }
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de la recherche des rapports.", e);
        }
        return rapportList;
    }


    public void generatePDF(ActionEvent event) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700); // Position du titre sur la page
            contentStream.showText("Rapport");
            contentStream.endText();

            // Position de départ du tableau
            float margin = 50;
            float yStart = 650;

            // Largeurs des colonnes
            float[] columnWidths = {150, 150, 150};

            // Utilisez les données déjà chargées dans la table
            ObservableList<Rapport> rapportList = table2.getItems();

            // Dessiner les en-têtes de colonne
            contentStream.setLineWidth(1);
            contentStream.moveTo(margin, yStart);
            contentStream.lineTo(margin + columnWidths[0] + columnWidths[1] + columnWidths[2], yStart);
            contentStream.stroke();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.newLineAtOffset(margin + 2, yStart - 15);
            contentStream.showText("Note");
            contentStream.newLineAtOffset(columnWidths[0], 0);
            contentStream.showText("Type");
            contentStream.newLineAtOffset(columnWidths[1], 0);
            contentStream.showText("Rdv ID");
            contentStream.endText();

            float nextY = yStart;

            // Dessiner les lignes de données
            for (Rapport rapport : rapportList) {
                nextY -= 20; // Décalage vertical pour la prochaine ligne
                contentStream.setLineWidth(1);
                contentStream.moveTo(margin, nextY);
                contentStream.lineTo(margin + columnWidths[0] + columnWidths[1] + columnWidths[2], nextY);
                contentStream.stroke();
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(margin + 2, nextY - 15);
                contentStream.showText(rapport.getNote());
                contentStream.newLineAtOffset(columnWidths[0], 0);
                contentStream.showText(rapport.getType());
                contentStream.newLineAtOffset(columnWidths[1], 0);
                contentStream.showText(String.valueOf(rapport.getRdvId()));
                contentStream.endText();
            }

            contentStream.close();

            // Choisissez l'emplacement où enregistrer le fichier PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.setInitialFileName("Rapport.pdf");
            File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

            // Enregistrez le document PDF
            if (file != null) {
                document.save(file);
                showAlert("Success", "PDF généré avec succès !");
            }

            document.close();
        } catch (IOException e) {
            showAlert("Error", "Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }
    private void afficherRepartitionRapport() {
        // Récupérer la liste des rapports
        ObservableList<Rapport> rapports = table2.getItems();

        // Créer une liste observable pour stocker les données du graphique circulaire
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Créer des variables pour compter le nombre de chaque type de rapport
        int consultationsCount = 0;
        int analysesCount = 0;
        int diabetiquesCount = 0;

        // Parcourir les rapports et compter le nombre de rapports pour chaque type
        for (Rapport rapport : rapports) {
            String type = rapport.getType();
            if (type.equals("consultation_psychologique")) {
                consultationsCount++;
            } else if (type.equals("resultat_analyses")) {
                analysesCount++;
            } else if (type.equals("diabetique")) {
                diabetiquesCount++;
            }
        }

        // Calculer le total de rapports
        double totalRapports = rapports.size();

        // Ajouter les données au graphique circulaire avec les pourcentages
        if (consultationsCount > 0) {
            double percentage = (consultationsCount / totalRapports) * 100;
            PieChart.Data consultationsData = new PieChart.Data("Consultations Psychologiques", percentage);
            consultationsData.nameProperty().bindBidirectional(consultationsData.pieValueProperty(), new NumberStringConverter());
            pieChartData.add(consultationsData);
        }
        if (analysesCount > 0) {
            double percentage = (analysesCount / totalRapports) * 100;
            PieChart.Data analysesData = new PieChart.Data("Résultats d'Analyses", percentage);
            analysesData.nameProperty().bindBidirectional(analysesData.pieValueProperty(), new NumberStringConverter());
            pieChartData.add(analysesData);
        }
        if (diabetiquesCount > 0) {
            double percentage = (diabetiquesCount / totalRapports) * 100;
            PieChart.Data diabetiquesData = new PieChart.Data("Diabétiques", percentage);
            diabetiquesData.nameProperty().bindBidirectional(diabetiquesData.pieValueProperty(), new NumberStringConverter());
            pieChartData.add(diabetiquesData);
        }

        // Définir les données dans le PieChart
        pieChart.setData(pieChartData);
    }


}