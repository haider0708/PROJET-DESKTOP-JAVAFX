package controllers;

//import javafx.beans.property.SimpleStringProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Ordonnance;
import models.Pharmacie;
import services.ServiceOrdonnance;
import utils.DBConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import java.util.List;


import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class OrdonnanceFXML implements Initializable {
    /* private Connection cnx;
     private PreparedStatement st;
     private ResultSet rs;*/
    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    @FXML
    private Button generateQRButton;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private TableView<Ordonnance> t;

    @FXML
    private TableColumn<Ordonnance, Date> colDate;

    @FXML
    private TableColumn<Ordonnance, String> colDescription;

    @FXML
    private TableColumn<Ordonnance, String> colMedecin;

    @FXML
    private TableColumn<Ordonnance, String> colPatient;
    @FXML
    private TableColumn<Ordonnance, Integer> colId;

    @FXML
    private TableColumn<Ordonnance, String> colNompharmacie;

    @FXML
    private Button btnAjouter;

    @FXML
    private DatePicker tfdate;

    @FXML
    private TextField tfdescription;

    @FXML
    private TextField tfmedecin;

    @FXML
    private TextField tfpatient;
    @FXML
    private ComboBox<String> comboph;
    @FXML
    private TextField fieldFilter;


    int id = 0;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        cnx = DBConnection.getInstance().getCnx();
        try{
            String sql = "SELECT DISTINCT nom FROM pharmacie";
            PreparedStatement statement = cnx.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            List<String> pharmacies = new ArrayList<>();
            while (rs.next()) {
                pharmacies.add(rs.getString("nom"));
            }
            ObservableList<String> options = FXCollections.observableArrayList(pharmacies);
            comboph.setItems(options);
        } catch(
                SQLException e)

        {
            e.printStackTrace();  // Gérer l'exception selon vos besoins
        }

        showOrdonnance();
        t.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->

        {
            if (newSelection != null) {
                // Afficher les données de l'élément sélectionné dans les champs de saisie
                tfmedecin.setText(newSelection.getNommedecin());
                tfpatient.setText(newSelection.getNompatient());
                tfdescription.setText(newSelection.getDescription());
                comboph.setValue(newSelection.getNompharmacie());
                comboph.setDisable(true); // Désactiver le champ pour empêcher la modification
            }
        });
        // Activer le tri sur la TableView
        t.setSortPolicy(param -> {
            ObservableList<Ordonnance> items = t.getItems();
            items.sort((o1, o2) -> {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                // Modifier cette partie selon la colonne sur laquelle vous voulez trier
                return o1.getNommedecin().compareTo(o2.getNommedecin());
            });
            return true;
        });

        // Filtrer les données lors de la saisie dans le champ de recherche
        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            t.setItems(filterOrdonnances(newValue));
        });

    }
    // Fonction pour filtrer les données des ordonnances en fonction du texte de recherche
    private ObservableList<Ordonnance> filterOrdonnances(String filterText) {
        ObservableList<Ordonnance> ordonnances = FXCollections.observableArrayList();
        for (Ordonnance ordonnance : getOrdonnances()) {
            if (ordonnance.getNommedecin().toLowerCase().contains(filterText.toLowerCase()) ||
                    ordonnance.getNompatient().toLowerCase().contains(filterText.toLowerCase()) ||
                    ordonnance.getDescription().toLowerCase().contains(filterText.toLowerCase()) ||
                    ordonnance.getNompharmacie().toLowerCase().contains(filterText.toLowerCase())) {
                ordonnances.add(ordonnance);
            }
        }
        return ordonnances;
    }





    @FXML
    void ajouterOrdonnance(ActionEvent event) {
        String nommedecin = tfmedecin.getText();
        String nompatient = tfpatient.getText();
        String description = tfdescription.getText();

        if (!validerSaisies(nommedecin, nompatient, description)) {
            return;
        }

        String insert = "INSERT INTO ordonnance(nommedecin, nompatient, description, pharmacie_id, nompharmacie) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ordonnance = cnx.prepareStatement(insert);
            ordonnance.setString(1, tfmedecin.getText());
            ordonnance.setString(2, tfpatient.getText());
            ordonnance.setString(3, tfdescription.getText());
            ordonnance.setInt(4, 0); // Utilisation de la valeur 0 pour pharmacie_id
            ordonnance.setString(5, comboph.getValue()); // Définir la valeur pour nompharmacie
            ordonnance.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "L'ordonnance a été ajoutée avec succès.");

            showOrdonnance();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout de l'ordonnance : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getDataO(MouseEvent event) {
        Ordonnance ordonnance = t.getSelectionModel().getSelectedItem();
        id = ordonnance.getId();
        tfmedecin.setText(ordonnance.getNommedecin());
        tfpatient.setText(ordonnance.getNompatient());
        tfdescription.setText(ordonnance.getDescription());
        //tfdate.setDisable(true);
        btnAjouter.setDisable(true);
    }

    public void showOrdonnance() {
        ObservableList<Ordonnance> list = getOrdonnances();
        t.setItems(list);
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("nommedecin"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("nompatient"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colNompharmacie.setCellValueFactory(new PropertyValueFactory<>("nompharmacie"));
    }

    //colDate.setCellValueFactory(new PropertyValueFactory<Ordonnance,Date>("datecreation"));



    public ObservableList<Ordonnance> getOrdonnances() {
        ObservableList<Ordonnance> ordonnances = FXCollections.observableArrayList();
        String query = "select* from ordonnance ";
        cnx = DBConnection.getInstance().getCnx();

        try {
            st = cnx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Ordonnance ordonnance = new Ordonnance();

                ordonnance.setNommedecin(rs.getString("nommedecin"));
                ordonnance.setNompatient(rs.getString("nompatient"));
                ordonnance.setDescription(rs.getString("Description"));
                ordonnance.setPharmacie_id(rs.getInt("pharmacie_id"));
                ordonnance.setNompharmacie(rs.getString("nompharmacie"));
                //LocalDate dateCreation = rs.getDate("datecreation").toLocalDate();
                //java.sql.Date sqlDate = rs.getDate("datecreation");
                //LocalDate localDate = sqlDate.toLocalDate();
                ordonnance.setId(rs.getInt("Id"));

                ordonnances.add(ordonnance);
                //System.out.println(ordonnances);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordonnances;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void modifierOrd(ActionEvent event) {
        String nommedecin =tfmedecin.getText();
        String nompatient =tfpatient.getText();
        String description =tfdescription.getText();
        if(!validerSaisies(nommedecin,nompatient,description)){return;}

        String update = "update ordonnance set nommedecin = ?, nompatient= ?,description =? where id =?";
        cnx = DBConnection.getInstance().getcnx();
        try {

            PreparedStatement ordonnance = cnx.prepareStatement(update);
            ordonnance.setString(1, tfmedecin.getText());
            ordonnance.setString(2, tfpatient.getText());
            ordonnance.setString(3, tfdescription.getText());
            ordonnance.setInt(4, id);
            ordonnance.executeUpdate();
            showOrdonnance();
            clear();
            showAlert(Alert.AlertType.INFORMATION, "Mise à Jour réussi", "L'ordonnance a étémise à jour avec succès.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void supprimerord(ActionEvent event) throws SQLException {
        String delete ="delete from ordonnance where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try {
            PreparedStatement pharmacie = cnx.prepareStatement(delete);
            pharmacie.setInt(1,id);
            pharmacie.executeUpdate();
            showOrdonnance();
            clear();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussi", "L'ordonnance a été supprimé avec succès.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void clearOrd(ActionEvent event) {clear();

    }
    void clear(){
        tfmedecin.setText(null);
        tfpatient.setText(null);
        tfdescription.setText(null);
        btnAjouter.setDisable(false);
    }

    public boolean validerSaisies(String nommedecin, String nompatient , String description) {
        // Vérifier si l'un des champs est vide
        if (nommedecin.isEmpty() || nompatient.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs.");
            return false; // Retourne false si l'un des champs est vide
        }

        // Validation du nom
        if (nommedecin.length() < 3 || nommedecin.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères et ne doit pas contenir de chiffres.");
            return false; // Retourne false si le nom ne respecte pas les critères
        }

        // Validation de l'adresse
        if (nompatient.length() < 3 || nommedecin.matches(".*\\d.*")){
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères.");
            return false; // Retourne false si l'adresse ne respecte pas les critères
        }

        // Validation du numéro de téléphone
        if (description.length() <5){
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères");
            return false; // Retourne false si le numéro de téléphone ne respecte pas les critères
        }

        return true; // Retourne true si toutes les validations sont passées avec succès
    }

    @FXML
    void handleGeneratePdfButton(ActionEvent event) throws DocumentException {
        try {
            String dest = "C:\\Users\\Ahmed Saadani\\Downloads\\simple_table.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            document.open();
            document.add(new Paragraph("Liste des Ordonnances"));

            // Retrieve posts data from the database
            ServiceOrdonnance serviceordonnance = new ServiceOrdonnance();
            List<Ordonnance> ordonnances = serviceordonnance.selectAll();

            // Create a table with 4 columns for post information
            PdfPTable table = new PdfPTable(4);

            // Add table headers with background color and text color
            addTableHeader(table);

            // Add data to table cells with alternating background colors
            boolean alternate = false;
            for (Ordonnance ordonnance : ordonnances) {
                addDataToTable(table, ordonnance, alternate);
                alternate = !alternate; // Toggle alternate flag
            }

            // Add the table to the document
            document.add(table);
            document.close();
            System.out.println("PDF generated successfully!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Function to add table headers with background color and text color
    private void addTableHeader(PdfPTable table) {
        PdfPCell headerCell;
        BaseColor headerColor = new BaseColor(173, 216, 230); // Light blue color for header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK); // Black text color for header

        headerCell = new PdfPCell(new Phrase("nommedecin", headerFont));
        headerCell.setBackgroundColor(headerColor);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("nompatient", headerFont));
        headerCell.setBackgroundColor(headerColor);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Description", headerFont));
        headerCell.setBackgroundColor(headerColor);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("nompharmacie", headerFont));
        headerCell.setBackgroundColor(headerColor);
        table.addCell(headerCell);
    }

    // Function to add data to table cells with alternating background colors
    private void addDataToTable(PdfPTable table, Ordonnance ordonnance, boolean alternate) {
        BaseColor backgroundColor = alternate ? BaseColor.WHITE : new BaseColor(240, 248, 255); // Alternating background colors

        PdfPCell cell;

        cell = new PdfPCell(new Phrase(ordonnance.getNommedecin()));
        cell.setBackgroundColor(backgroundColor);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(ordonnance.getNompatient()));
        cell.setBackgroundColor(backgroundColor);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(ordonnance.getDescription()));
        cell.setBackgroundColor(backgroundColor);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(ordonnance.getNompharmacie()));
        cell.setBackgroundColor(backgroundColor);
        table.addCell(cell);
    }

    public void gotoPharmacie(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjouterPharmacieFXML.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        // Get the controller
        AjouterPharmacieFXML controller = fxmlLoader.getController();

        // Set the stage
        Stage stage = new Stage();
        controller.setStage(stage); // Pass the stage to the controller
        stage.setScene(new Scene(root1));
        stage.show();
    }
    @FXML
    void statistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stato.fxml"));
            Parent parent = loader.load();

            // Add the loaded parent node to the scene or UI hierarchy
            // For example:
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately, e.g., logging or displaying an error message
        }
    }
    @FXML
    private void generateQRCode() {
        String medecinNom = tfmedecin.getText().trim();
        String patientNom = tfpatient.getText().trim();
        String Description = tfdescription.getText().trim();

        // Concatenate the cheque details into a single string
        String chequeDetails = "Votre Ordonnace : " + medecinNom + "\nPatienet: " + patientNom + "\nDescription: " + Description;


        // Set the width and height of the QR code
        int width = 350;
        int height = 350;


        // Generate the QR code image based on the cheque details
        javafx.scene.image.Image qrImage = generateQRCodeImage(chequeDetails, width, height);


        // Set the generated QR code image to the ImageView
        qrCodeImageView.setImage(qrImage);
    }
    private javafx.scene.image.Image generateQRCodeImage(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // Adjust margin as needed


        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);


            return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (WriterException | IOException e) {
            e.printStackTrace(); // Handle error gracefully in your application
            return null;
        }
    }
}
