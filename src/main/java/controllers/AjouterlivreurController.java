/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package controllers;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.livreur;
import services.livreurService;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AjouterlivreurController implements Initializable {
    @FXML
    private Label NAME;

    @FXML
    private ImageView IMAGE;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOrders;


    @FXML
    private Pane pnlOverview;
    @FXML
    private TextField nomevField;
    @FXML
    private TextField prenomevField;
    @FXML
    private TextField numerotelField;

    @FXML
    private ImageView QrCode;


    @FXML
    private TextField imageevField;


    @FXML
    private TableView<livreur> livreurTv;


    @FXML
    private TableColumn<livreur, String> imageevTv;

    @FXML
    private TableColumn<livreur, String> nomevTv;
    @FXML
    private TableColumn<livreur, String> prenomevTv;
    @FXML
    private TableColumn<livreur, Integer> numerotelTv;





    livreurService Ev = new livreurService();


    @FXML
    private TextField idmodifierField;

    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        getevs();


    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnOrders) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(AjouterlivreurController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @FXML
    void LOGOUT(MouseEvent event) {









    }


    @FXML
    private void ajouterlivreur(ActionEvent ev) {
        int part = 0;
        if (imageevField.getText().isEmpty() || nomevField.getText().isEmpty() || prenomevField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        } else if (nomevField.getText().length() < 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur !");
            alert.setContentText("Le nom doit contenir au moins 4 caractères.");
            alert.showAndWait();
        } else if (prenomevField.getText().length() < 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur !");
            alert.setContentText("Le prénom doit contenir au moins 4 caractères.");
            alert.showAndWait();
        } else if (!nomevField.getText().matches("^[a-zA-Z]+$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur !");
            alert.setContentText("Le nom doit contenir uniquement des lettres.");
            alert.showAndWait();
        } else if (!prenomevField.getText().matches("^[a-zA-Z]+$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur !");
            alert.setContentText("Le prénom doit contenir uniquement des lettres.");
            alert.showAndWait();
        } else if (!numerotelField.getText().matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Please enter a valid Tunisian phone number (8 digits)");
            alert.showAndWait();
        } else if (numerotelField.getText().matches(".*(\\d)\\1{6,}.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Please enter a phone number with at most 7 consecutive identical digits");
            alert.showAndWait();
        } else {
            livreur e = new livreur();

            e.setNom(nomevField.getText());
            e.setPrenom(prenomevField.getText());
            e.setNumero_tel(Integer.parseInt(numerotelField.getText()));
            e.setImage(imageevField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("livreur add");
            alert.setContentText("livreur added successfully!");
            alert.showAndWait();

            try {
                Ev.ajouterlivreur(e);
                reset();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            getevs();
        }
    }



    private void reset() {

        nomevField.setText("");
        prenomevField.setText("");
        numerotelField.setText("");

        imageevField.setText("");

    }

    public void getevs() {
        try {
            List<livreur> livreur = Ev.recupererlivreur();
            ObservableList<livreur> olp = FXCollections.observableArrayList(livreur);
            livreurTv.setItems(olp);

            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image"));

            nomevTv.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomevTv.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            numerotelTv.setCellValueFactory(new PropertyValueFactory<>("numerotel"));

        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }

    @FXML
    private void modifierlivreur(ActionEvent ev) throws SQLException {
        livreur e = new livreur();
        e.setId(Integer.parseInt(idmodifierField.getText()));

        e.setNom(nomevField.getText());
        e.setPrenom(prenomevField.getText());
        e.setNumero_tel(Integer.parseInt(numerotelField.getText()));



        e.setImage(imageevField.getText());
        Ev.modifierlivreur(e);
        reset();
        getevs();
    }

    @FXML
    private void supprimerlivreur(ActionEvent ev) {
        livreur e = livreurTv.getItems().get(livreurTv.getSelectionModel().getSelectedIndex());
            try {
            Ev.supprimerlivreur(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterlivreurController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("livreur delete");
        alert.setContentText("livreur deleted successfully!");
        alert.showAndWait();
        getevs();
    }

    @FXML
    private void afficherlivreur(ActionEvent ev) {
        try {
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Front.fxml")));
            livreurTv.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void stat(MouseEvent event) {
        try {
            // Load the FXML file for the new interface
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Statistics.fxml")));

            // Create a new stage
            Stage newStage = new Stage();

            // Set the scene of the new stage to the loaded interface
            newStage.setScene(new Scene(loader));

            // Show the new stage
            newStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void nav(ActionEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherCommande.fxml"));
            Pane addUserView = loader.load();
            pnlMenus.getChildren().setAll(addUserView);
            pnlMenus.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void choisirev(MouseEvent ev) throws IOException {
        livreur e = livreurTv.getItems().get(livreurTv.getSelectionModel().getSelectedIndex());
        idmodifierField.setText(String.valueOf(e.getId()));

        imageevField.setText(e.getImage());
        nomevField.setText(e.getNom());
        prenomevField.setText(e.getPrenom());
        numerotelField.setText(String.valueOf(e.getNumero_tel()));

        String path = e.getImage();
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        imageview.setImage(img);


        //////// qr
        String filename = Ev.GenerateQrev(e);
        System.out.println("filename lenaaa " + filename);
        String path1="C:\\xampp\\htdocs\\imgQr\\qrcode"+filename;
        File file1=new File(path1);
        Image img1 = new Image(file1.toURI().toString());
        //Image image = new Image(getClass().getResourceAsStream("src/utils/img/" + filename));
        QrCode.setImage(img1);
    }

    @FXML
    private void uploadImage(ActionEvent ev) throws FileNotFoundException, IOException {
        Random rand = new Random();
        int x = rand.nextInt(1000);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        String DBPath = "C:\\\\xampp\\\\htdocs\\\\imageP\\\\" + x + ".jpg";
        if (file != null) {
            FileInputStream Fsource = new FileInputStream(file.getAbsolutePath());
            FileOutputStream Fdestination = new FileOutputStream(DBPath);
            BufferedInputStream bin = new BufferedInputStream(Fsource);
            BufferedOutputStream bou = new BufferedOutputStream(Fdestination);
            String path = file.getAbsolutePath();
            Image img = new Image(file.toURI().toString());
            imageview.setImage(img);
            imageevField.setText(DBPath);
            int b = 0;
            while (b != -1) {
                b = bin.read();
                bou.write(b);
            }
            bin.close();
            bou.close();
        } else {
            System.out.println("error");
        }
    }

    @FXML
    private void rechercherev(KeyEvent ev) {
        livreurService bs = new livreurService();
        ObservableList<livreur> filter = bs.chercherev(rechercher.getText());
        populateTable(filter);
    }
    @FXML
    private void pdfabonn(ActionEvent abonn) throws FileNotFoundException, SQLException, IOException {
        // livreur tab_Recselected = livreurTv.getSelectionModel().getSelectedItem();
        long millis = System.currentTimeMillis();
        java.sql.Date DateRapport = new java.sql.Date(millis);

        String DateLyoum = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(DateRapport);//yyyyMMddHHmmss
        System.out.println("Date d'aujourdhui : " + DateLyoum);

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(String.valueOf(DateLyoum + ".pdf")));//yyyy-MM-dd
            document.open();
            Paragraph ph1 = new Paragraph("Voici un rapport détaillé de notre application qui contient tous les livreurs . Pour chaque livreur, nous fournissons des informations telles que la date d'Aujourd'hui :" + DateRapport );
            Paragraph ph2 = new Paragraph(".");
            PdfPTable table = new PdfPTable(4);
            //On créer l'objet cellule.
            PdfPCell cell;
            //contenu du tableau.
            table.addCell("Nom");
            table.addCell("Prenom");
            table.addCell("Numero");
            table.addCell("image");

            livreur r = new livreur();
            Ev.recupererlivreur().forEach(new Consumer<livreur>() {
                @Override
                public void accept(livreur e) {
                    table.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(String.valueOf(e.getNom()));
                    table.addCell(String.valueOf(e.getPrenom()));
                    table.addCell(String.valueOf(e.getNumero_tel()));
                    try {
                        // Créer un objet Image à partir de l'image
                        String path = e.getImage();
                        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(path);

                        // Définir la taille de l'image dans le tableau
                        img.scaleToFit(100, 100); // Définir la largeur et la hauteur de l'image

                        // Ajouter l'image à la cellule du tableau
                        PdfPCell cell = new PdfPCell(img);
                        table.addCell(cell);
                    } catch (Exception ex) {
                        table.addCell("Erreur lors du chargement de l'image");
                    }
                }
            });
            document.add(ph1);
            document.add(ph2);
            document.add(table);
        } catch (Exception e) {
            System.out.println(e);
        }
        document.close();

        /*///Open FilePdf
        File file = new File(DateLyoum + ".pdf");
        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) //checks file exists or not  
        {
            desktop.open(file); //opens the specified file   
        }*/
    }

    private void populateTable(ObservableList<livreur> branlist) {livreurTv.setItems(branlist);
    }
}
