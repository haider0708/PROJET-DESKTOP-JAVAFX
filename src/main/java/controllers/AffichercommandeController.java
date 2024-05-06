/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package controllers;

import models.commande;
import models.livreur;

import java.io.IOException;

import services.commandeService;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import services.livreurService;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class AffichercommandeController implements Initializable {

    @FXML
    private TableView<commande> tablecommande;
     livreurService ab=new livreurService();

    @FXML
    private TableColumn<commande, Integer> idevTv;

    @FXML
    private TableColumn<commande, String> nom_clientevTv;
    @FXML
    private TableColumn<commande, String> addresse_clientevTv;
    @FXML
    private TableColumn<commande, String> numero_clientevTv;

    @FXML
    private Button modifierPartBtn;
    @FXML

    private Button supprimerPartBtn;
    @FXML
    private TextField nom_clientevField;
    @FXML
    private TextField addresse_clientevField;
    @FXML
    private TextField numero_clientevField;
    @FXML
    private TextField titreevField;
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
    private Button ajouter;
    @FXML
    private TextField idread;

    @FXML
    private TextField idevField;

    @FXML
    private TextField chercherevField;
    @FXML
    private Button commanderevButton;
    commandeService Ps=new commandeService();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        getComment();
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
    private void rechercherlivreur(KeyEvent ev) {
        try {
            List<livreur> livreur = ab.chercherev(chercherevField.getText());

            int row = 0;
            int column = 0;
            for (int i = 0; i < livreur.size(); i++) {
                //chargement dynamique d'une interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/livreur.fxml"));
                AnchorPane pane = loader.load();
                //passage de parametres
                livreurController controller = loader.getController();
                controller.setlivreur(livreur.get(i));
                controller.setIdev(livreur.get(i).getId());


            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void modifiercommande(ActionEvent ev) throws SQLException {
        
         commande pa = new commande();
        pa.setId_commande(Integer.valueOf(idread.getText()));
        pa.setId(Integer.valueOf(idevField.getText()));


        pa.setNom_client(nom_clientevField.getText());
        pa.setAddresse_client(addresse_clientevField.getText());
        pa.setNumero_client(numero_clientevField.getText());

        //pa.setCreated(datepartField.getText());
       
        Ps.modifiercommande(pa);
        resetPart();
        getComment();
           
        
    }
    @FXML
    private void commanderev(MouseEvent ev) throws SQLException {
        if (idevField.getText().isEmpty() || nom_clientevField.getText().isEmpty() || addresse_clientevField.getText().isEmpty() || numero_clientevField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        } else if (nom_clientevField.getText().length() < 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur !");
            alert.setContentText("Le nom doit contenir au moins 4 caractères.");
            alert.showAndWait();
        } else if (!nom_clientevField.getText().matches("^[a-zA-Z]+$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur !");
            alert.setContentText("Le nom doit contenir uniquement des lettres.");
            alert.showAndWait();
        } else if (!numero_clientevField.getText().matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Please enter a valid Tunisian phone number (8 digits)");
            alert.showAndWait();
        } else if (numero_clientevField.getText().matches(".*(\\d)\\1{6,}.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Please enter a phone number with at most 7 consecutive identical digits");
            alert.showAndWait();
        } else {
            commande p = new commande(Integer.parseInt(idevField.getText()), nom_clientevField.getText(), addresse_clientevField.getText(), numero_clientevField.getText());

            Ps.ajoutercommande(p);

            getComment();
        }
    }



    @FXML
    private void supprimercommande(ActionEvent ev) {
         commande p = tablecommande.getItems().get(tablecommande.getSelectionModel().getSelectedIndex());
      
        try {
            Ps.Deletecommande(p);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterlivreurController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("commande delete");
        alert.setContentText("commande deleted successfully!");
        alert.showAndWait();
        getComment();
     
    }

    @FXML
    private void choisircommande(MouseEvent ev)  throws IOException {

        commande part = tablecommande.getItems().get(tablecommande.getSelectionModel().getSelectedIndex());
        
        idread.setText(String.valueOf(part.getId_commande()));
        idevField.setText(String.valueOf(part.getId()));
        nom_clientevField.setText(String.valueOf(part.getNom_client()));
        addresse_clientevField.setText(String.valueOf(part.getAddresse_client()));
        numero_clientevField.setText(String.valueOf(part.getNumero_client()));

        //datepartField.setValue((part.getCreated()));
        
    }
    @FXML
    private void nav(ActionEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterlivreur.fxml"));
            Pane addUserView = loader.load();
            pnlMenus.getChildren().setAll(addUserView);
            pnlMenus.toFront();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void getCommande(){
        try {


            // TODO
            List<commande> part = Ps.recupererCommande();
            ObservableList<commande> olp = FXCollections.observableArrayList(part);
            tablecommande.setItems(olp);

            idevTv.setCellValueFactory(new PropertyValueFactory<>("id"));

            nom_clientevTv.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
            addresse_clientevTv.setCellValueFactory(new PropertyValueFactory<>("addresse_client"));
            numero_clientevTv.setCellValueFactory(new PropertyValueFactory<>("numero_client"));

            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }
    public void getComment(){
        try {


            // TODO
            List<commande> part = Ps.recupererComment();
            ObservableList<commande> olp = FXCollections.observableArrayList(part);
            tablecommande.setItems(olp);

            idevTv.setCellValueFactory(new PropertyValueFactory<>("id"));

            nom_clientevTv.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
            addresse_clientevTv.setCellValueFactory(new PropertyValueFactory<>("addresse_client"));
            numero_clientevTv.setCellValueFactory(new PropertyValueFactory<>("numero_client"));

            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }
    
    public void resetPart() {
        idread.setText("");
        idevField.setText("");


        
    }
   
    
}


 