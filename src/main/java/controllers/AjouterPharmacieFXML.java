package controllers;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.EmailSender;
import models.Pharmacie;
import utils.DBConnection;
import javafx.scene.control.TextFormatter;//controle de saisie numero

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static models.EmailSender.AjoutPharmacieEmail;

public class AjouterPharmacieFXML implements Initializable {

    public Button btnAjout;
    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;
    @FXML
    private ImageView image;

    @FXML
    private TextField imgField;
    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfAdresse;

    @FXML
    private TextField tfNumero;

    @FXML
    private TableColumn<Pharmacie, String> colAdresse;

    @FXML
    private TableColumn<Pharmacie, Integer> colId;
    @FXML
    private TableColumn<Pharmacie, String> colNom;
    @FXML
    private TableColumn<Pharmacie, Integer> colTelephone;
    @FXML
    private TableView<Pharmacie> table;
    @FXML
    private TextField filterFieldP;
    private String xamppFolderPath="C:/xampp/htdocs/img/";
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    int id =0;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPharmacie();
        //condition de stopper l utilisateur s il depasse 8 chiffres
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 8) {
                return change;
            } else {
                return null;
            }

        });
        // Appliquer le TextFormatter au champ de saisie du numéro de téléphone
        tfNumero.setTextFormatter(textFormatter);
        // Activer le tri
        table.setSortPolicy(param -> {
            ObservableList<Pharmacie> items = table.getItems();
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
        filterFieldP.textProperty().addListener((observable, oldValue, newValue) -> {
            table.setItems(filterPharmacies(newValue));
        });
    }
    private ObservableList<Pharmacie> filterPharmacies(String filterText) {
        ObservableList<Pharmacie> pharmacies = FXCollections.observableArrayList();
        for (Pharmacie pharmacie : getPharmacies()) {
            if (pharmacie.getNom().toLowerCase().contains(filterText.toLowerCase())) {
                pharmacies.add(pharmacie);
            }
        }
        return pharmacies;
    }
    public ObservableList<Pharmacie>getPharmacies(){
        ObservableList<Pharmacie> pharmacies = FXCollections.observableArrayList();
        String query = "select* from pharmacie ";
        cnx = DBConnection.getInstance().getCnx();
        try {
            st = cnx.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                Pharmacie pharmacie = new Pharmacie();
                pharmacie.setId(rs.getInt("Id"));
                pharmacie.setNom(rs.getString("Nom"));
                pharmacie.setAdresse(rs.getString("Adresse"));
                pharmacie.setNumerotelephone(rs.getInt("NumeroTelephone"));
                pharmacies.add(pharmacie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pharmacies;
    }
    //affichage tableau
    public void showPharmacie()
    {
        ObservableList<Pharmacie> list = getPharmacies();
        table.setItems(list);
        //colId.setCellValueFactory(new PropertyValueFactory<Pharmacie,Integer>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<Pharmacie,String>("Nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<Pharmacie,String>("Adresse"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<Pharmacie,Integer>("numeroTelephone"));
    }
    @FXML
    void ajouterPharmacie(ActionEvent event) {

        String nom = tfNom.getText();
        String adresse = tfAdresse.getText();
        String numeroTelephone = tfNumero.getText();
        String img =imgField.getText();

        if (!validateInput(nom, adresse, numeroTelephone)) {
            return;
        }
        String insert = "insert into pharmacie(Nom,Adresse,NumeroTelephone,img) values(?,?,?,?)";
        cnx=DBConnection.getInstance().getCnx();
        try{
            PreparedStatement pharmacie = cnx.prepareStatement(insert);
            pharmacie.setString(1,tfNom.getText());
            pharmacie.setString(2,tfAdresse.getText());
            pharmacie.setInt(3, Integer.parseInt(tfNumero.getText()));
            pharmacie.setString(4,img);
            AjoutPharmacieEmail("rania_driss@yahoo.com","");
            pharmacie.executeUpdate();
            //clear();
            showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "La pharmacie a été ajoutée avec succès Verifier votre boite mail.");

            showPharmacie();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void getData(MouseEvent event) {
        Pharmacie pharmacie= table.getSelectionModel().getSelectedItem();
        id = pharmacie.getId();
        tfNom.setText(pharmacie.getNom());
        tfAdresse.setText(pharmacie.getAdresse());
        tfNumero.setText(String.valueOf(pharmacie.getNumeroTelephone()));
        btnAjout.setDisable(true);

    }
    void clear(){
        tfNom.setText(null);
        tfNumero.setText(null);
        tfAdresse.setText(null);
        btnAjout.setDisable(false);
    }
    @FXML
    void MettreaJour(ActionEvent event) throws SQLException{
        String nom = tfNom.getText();
        String adresse = tfAdresse.getText();
        String numeroTelephone = tfNumero.getText();

        if (!validateInput(nom, adresse, numeroTelephone)) {
            return;
        }
        String update = "update pharmacie set nom = ?, adresse= ?,numerotelephone =? where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try{
            PreparedStatement pharmacie = cnx.prepareStatement(update);
            pharmacie.setString(1,tfNom.getText());
            pharmacie.setString(2,tfAdresse.getText());
            pharmacie.setString(3,tfNumero.getText());
            pharmacie.setInt(4,id);
            pharmacie.executeUpdate();
            showPharmacie();
            clear();
            showAlert(Alert.AlertType.INFORMATION, "Mise à Jour  réussi", "La pharmacie a été mise a jour  avec succès.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void supprimerPharmacie(ActionEvent event) throws SQLException {
        String delete ="delete from pharmacie where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try {
            PreparedStatement pharmacie = cnx.prepareStatement(delete);
            pharmacie.setInt(1,id);
            pharmacie.executeUpdate();
            showPharmacie();
            clear();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussi", "La pharmacie a été supprimé avec succès.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void clearField(ActionEvent event) {
        clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
    private boolean validateInput(String nom, String adresse, String numeroTelephone) {
        if (nom.isEmpty() || adresse.isEmpty() || numeroTelephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs.");
            return false;
        }

        // Validation du nom
        if (nom.length() < 3 || nom.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères et ne doit pas contenir de chiffres.");
            return false;
        }

        // Validation de l'adresse
        if (adresse.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'adresse doit contenir au moins 3 caractères.");
            return false;
        }

        // Validation du numéro de téléphone
        if (numeroTelephone.length() != 8 || !numeroTelephone.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return false;
        }

        return true;
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
    void frontpharmacie(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontPharmacieFXML.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        // Get the controller
        frontPharmacie controller = fxmlLoader.getController();

        // Set the stage
        Stage stage = new Stage();
        controller.setStage(stage); // Pass the stage to the controller
        stage.setScene(new Scene(root1));
        stage.show();

    }




}

