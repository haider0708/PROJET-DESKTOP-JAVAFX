package controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;

import com.itextpdf.text.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javafx.stage.FileChooser;
import utils.DBConnection;
//import javax.swing.text.html.ImageView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Produit;
import services.ServiceProduit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import utils.EmailUtil;

import javax.mail.MessagingException;


public class AjouterProduitFXML implements Initializable {
        public Button btnajouter;
        Connection cnx = null;
        PreparedStatement st = null;
        ResultSet rs = null;
    @FXML
    private Button btnstat;

        @FXML
        private ScrollPane scroll;
        @FXML
        private GridPane grid;
       /* @FXML
        private ImageView img;*/
       @FXML
       private TextField fieldFilter;
        @FXML
        private TextField tfDescription;
        @FXML
        private TableColumn<Produit, String> colnomcategorie;
        @FXML
        private ComboBox<String> combop;
        @FXML
        private TextField tfNom;

        @FXML
        private TextField tfPrix;
        @FXML

        private TableColumn<Produit, String> colDescription;
        @FXML
        private Button Parcourir;
        @FXML
        private ImageView image;
        @FXML
        private TextField imgField;

        @FXML
        private Button generateQRButton;

        @FXML
        private ImageView qrCodeImageView;

        @FXML
        private TableColumn<Produit, Integer> colId;

        @FXML
        private TableColumn<Produit, String> colNom;

        @FXML
        private TableColumn<Produit, Integer> colPrix;

        @FXML
        private TableView<Produit> table;

    @FXML
    private TableColumn<Produit , String>nomLabel;
    @FXML
    private TableColumn<Produit , Integer> prixLabel;
        private String xamppFolderPath="C:/xampp/htdocs/img/";
        int id = 0;
       // private Panier panier;
        ServiceProduit ServiceProduit = new ServiceProduit();
        @Override
       public void initialize(URL url, ResourceBundle resourceBundle) {
           cnx = DBConnection.getInstance().getCnx(); // Initialize cnx here
           grid = new GridPane();
          //  this.panier = new Panier();
           try {
               String sql = "SELECT DISTINCT nomcategorie FROM categorie";
               PreparedStatement statement = cnx.prepareStatement(sql);

               ResultSet rs = statement.executeQuery();
               List<String> produits = new ArrayList<>();
               while (rs.next()) {
                 produits.add(rs.getString("nomcategorie"));
               }
               ObservableList<String> options = FXCollections.observableArrayList(produits);
               combop.setItems(options);

           } catch (SQLException e) {
               e.printStackTrace();  // Handle the exception according to your needs
           }

           showProduit();
           table.getSelectionModel()
                   .selectedItemProperty()
                   .addListener((obs, oldSelection, newSelection) -> {
                       if (newSelection != null) {
                           // Display data of the selected item in the input fields
                           tfNom.setText(newSelection.getNom());
                           tfPrix.setText(String.valueOf(newSelection.getPrix()));
                           tfDescription.setText(newSelection.getDescription());
                           combop.setValue(newSelection.getNomcategorie());
                           combop.setDisable(true); // Disable the field to prevent modification
                       }
                   });
           // Activer le tri sur la TableView
           table.setSortPolicy(param -> {
               ObservableList<Produit> items = table.getItems();
               items.sort((o1, o2) -> {
                   if (o1 == null || o2 == null) {
                       return 0;
                   }
                   // Modifier cette partie selon la colonne sur laquelle vous voulez trier
                   return o1.getNom().compareTo(o2.getNom());
               });
               return true;
           });

           // Filtrer les données lors de la saisie dans le champ de recherche
           fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
               table.setItems(filterProduits(newValue));
           });
       }

        // Fonction pour filtrer les données des ordonnances en fonction du texte de recherche
        private ObservableList<Produit> filterProduits(String filterText) {
            ObservableList<Produit> produits = FXCollections.observableArrayList();
            for (Produit ordonnance : getproduits()) {
                String prixString = String.valueOf(ordonnance.getPrix());
                if (ordonnance.getNom().toLowerCase().contains(filterText.toLowerCase()) ||
                        ordonnance.getDescription().toLowerCase().contains(filterText.toLowerCase()) ||
                        prixString.toLowerCase().contains(filterText.toLowerCase()))  {
                    produits.add(ordonnance);
                }
            }
            return produits;
        }
        public ObservableList<Produit> getproduits(){
            ObservableList<Produit> produits = FXCollections.observableArrayList();
            String query = "select * from produit ";
            cnx = DBConnection.getInstance().getCnx();

            try {
                st = cnx.prepareStatement(query);
                rs = st.executeQuery();
                while(rs.next()){
                    Produit produit = new Produit();
                    produit.setId(rs.getInt("Id"));
                    produit.setNom(rs.getString("Nom"));
                    produit.setPrix(rs.getInt("Prix"));
                    produit.setDescription(rs.getString("Description"));
                    produit.setNomcategorie(rs.getString("nomcategorie"));

                    System.out.println("Nom Catégorie: " + produit.getNomcategorie()); // Debug output
                    produits.add(produit);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return produits;
        }

        public void showProduit()
        {
            ObservableList<Produit> list = getproduits();
            table.setItems(list);
           // colId.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("Prix"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
            colnomcategorie.setCellValueFactory(new PropertyValueFactory<>("nomcategorie"));

        }
        public void parcourirImage(ActionEvent event) {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("Choisi une image");
            Stage stage = new Stage();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG","*.jpg"),
                    new FileChooser.ExtensionFilter("JPEG","*.jpeg"),
                    new FileChooser.ExtensionFilter("PNG","*.png")
            );
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                Path source = file.toPath();
                String fileName = file.getName();
                Path destination = Paths.get(xamppFolderPath + fileName);
                String imgURL=xamppFolderPath+fileName;
                try {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    imgField.setText(imgURL);
                    Image image1= new Image("file:" +imgURL);
                    image.setImage(image1);


                } catch (IOException ex) {
                    System.out.println("Could not get the image");
                    ex.printStackTrace();
                }
            } else {
                System.out.println("No file selected");
            }

        }
        @FXML
        void ajouterProduit(ActionEvent event) {
            String nom = tfNom.getText();
            String prixText = tfPrix.getText();
            String description = tfDescription.getText();
            String img = imgField.getText();

            // Validate input
            if (!validateInput(nom, prixText, description)) {
                return; // Exit the method if the data is not valid
            }

            // SQL insert statement
            String insert = "INSERT INTO produit(Nom, Prix, Description, categorie_id, nomcategorie, img) VALUES(?, ?, ?, ?, ?, ?)";

            // Using try-with-resources to automatically close the connection
            try (
                    //Connection cnx = DBConnection.getInstance().getCnx();
                 PreparedStatement produit = cnx.prepareStatement(insert)) {

                // Set values for prepared statement
                produit.setString(1, nom);
                produit.setInt(2, Integer.parseInt(prixText));
                produit.setString(3, description);
                produit.setInt(4, 0);
                produit.setString(5, combop.getValue());
                produit.setString(6, img);

                // Execute the insert statement
                produit.executeUpdate();

                // Clear input fields and update the table
                clear();
                showProduit();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "Le produit a été ajouté avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de l'ajout du produit : " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de l'ajout du produit.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le prix doit être un nombre entier.");
            }
        }
        @FXML
        void getData(MouseEvent event) {

            Produit produit= table.getSelectionModel().getSelectedItem();
            id = produit.getId();
            tfNom.setText(produit.getNom());
            tfPrix.setText(String.valueOf(produit.getPrix()));
            tfDescription.setText(produit.getDescription());

            btnajouter.setDisable(true);

        }
        void clear(){
            tfNom.setText(null);
            tfPrix.setText(null);
            tfDescription.setText(null);
            btnajouter.setDisable(false);
        }
        @FXML
        void Modifier(ActionEvent event)  throws SQLException{
            String nom = tfNom.getText();
            String prixText = tfPrix.getText();
            String description = tfDescription.getText();

            if (!validateInput(nom, prixText, description)) {
                return; // Sortie de la méthode si les données ne sont pas valides
            }
                String update = "update produit set nom = ?, prix= ?,description =? where id =?";
                cnx =DBConnection.getInstance().getCnx();
                try{
                    PreparedStatement produit = cnx.prepareStatement(update);
                    produit.setString(1,tfNom.getText());
                    produit.setString(2,tfPrix.getText());
                    produit.setString(3,tfDescription.getText());
                    produit.setInt(4,id);
                    produit.executeUpdate();
                    showProduit();
                    showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Le produit a été modifiée avec succès.");
                    clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        }

        @FXML
        void Supprimer(ActionEvent event) throws SQLException, MessagingException {
            String Nom = tfNom.getText();
            String prix = tfPrix.getText();
            String delete ="delete from produit where id =?";
            cnx =DBConnection.getInstance().getCnx();
            try {
                PreparedStatement produit = cnx.prepareStatement(delete);
                produit.setInt(1,id);
                produit.executeUpdate();
                showProduit();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussi", "Le produit a été supprimer avec succès.");
                clear();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
// Envoi d'un e-mail pour informer l'utilisateur

            String recipient = "ahmedsaadani02@gmail.com"; // Remplacez par l'adresse e-mail de l'utilisateur
            String subject = "Produit en rupture ";

            String body = "Bonjour,\n\nCe Produit est en rupture de stock dans l entrepot  .\n\n";

            // Ajouter les détails du crédit au corps du message
            body += "- Nom : " + Nom + "\n" +
                    "- Prix " + prix + "DT" ;

            body += "Cordialement,\nDevLab";

            // Envoyer l'e-mail avec le corps et le logo

            EmailUtil.sendEmail(recipient, subject, body);

            System.out.println("E-mail envoyé à l'utilisateur pour informer la création du crédit !");

        }
        @FXML
        void clearFieled(ActionEvent event) {
         clear();

        }

        private void showAlert(Alert.AlertType type, String title, String content) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.show();
        }
        private boolean validateInput(String nom, String prixText, String description) {
            if (nom.isEmpty() || prixText.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis.");
                return false;
            }
            if (nom.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères.");
                return false;
            }

            // Regular expression to check if the nom contains any numbers
            if (nom.matches(".*\\d.*")) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom ne doit pas contenir des chiffres.");
                return false;
            }

            try {
                int prix = Integer.parseInt(prixText);
                if (prix < 0 || prix > 999) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le prix doit être entre 0 et 999.");
                    return false;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un prix valide (nombre entier).");
                return false;
            }

            if (description.length() < 5) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères.");
                return false;
            }

            return true;
        }

        @FXML
        void frontproduit(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontProduitFXML.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            // Get the controller
            frontProduit controller = fxmlLoader.getController();

            // Set the stage
            Stage stage = new Stage();
            controller.setStage(stage); // Pass the stage to the controller
            stage.setScene(new Scene(root1));
            stage.show();

        }
        @FXML
        private void generateQRCode() {
            String produitNom = tfNom.getText().trim();
            String prix = tfPrix.getText().trim();


            // Concatenate the cheque details into a single string
            String chequeDetails = "Votre Produit: " + produitNom + "\nPrix: " + prix +"DT";


            // Set the width and height of the QR code
            int width = 350;
            int height = 350;


            // Generate the QR code image based on the cheque details
            Image qrImage = generateQRCodeImage(chequeDetails, width, height);


            // Set the generated QR code image to the ImageView
            qrCodeImageView.setImage(qrImage);
        }

        private Image generateQRCodeImage(String text, int width, int height) {
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
    public void setProduitInfo(String nom, int prix) {
        // Afficher le nom et le prix du produit où vous le souhaitez dans votre interface utilisateur
        // Par exemple, vous pouvez les afficher dans des libellés
        // Par exemple, si vous avez un label nomLabel et un label prixLabel :
        nomLabel.setText(nom);
        prixLabel.setText(String.valueOf(prix));
    }
    @FXML
    void stat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stat.fxml"));
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
    void handleGeneratePdfButton(ActionEvent event) throws DocumentException {
        try {
            String dest = "C:\\Users\\Ahmed Saadani\\Downloads\\simple_table.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            document.open();
            document.add(new Paragraph("Liste des Produits"));

            // Retrieve posts data from the database
            ServiceProduit serviceproduit = new ServiceProduit();
            List<Produit> produits = serviceproduit.selectAll();


            // Create a table with 3 columns for post information
            PdfPTable table = new PdfPTable(3);

            // Add table headers with background color and text color
            addTableHeader(table);

            // Add data to table cells with alternating background colors
            boolean alternate = false;
            for (Produit produit : produits) {
                addDataToTable(table, produit, alternate);
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

    private void addTableHeader(PdfPTable table) {
        PdfPCell headerCell;
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE); // White text color for header

        headerCell = new PdfPCell(new Phrase("nom", headerFont));
        headerCell.setBackgroundColor(new BaseColor(222, 184, 135)); // Light beige color for header
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("prix", headerFont));
        headerCell.setBackgroundColor(new BaseColor(222, 184, 135)); // Light beige color for header
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("description", headerFont));
        headerCell.setBackgroundColor(new BaseColor(222, 184, 135)); // Light beige color for header
        table.addCell(headerCell);
    }

    private void addDataToTable(PdfPTable table, Produit produit, boolean alternate) {
        BaseColor cellColor = new BaseColor(255, 255, 255); // White color for cells

        PdfPCell cell;

        cell = new PdfPCell(new Phrase(produit.getNom()));
        cell.setBackgroundColor(cellColor);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(produit.getPrix())));
        cell.setBackgroundColor(cellColor);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(produit.getDescription()));
        cell.setBackgroundColor(cellColor);
        table.addCell(cell);
    }

}



