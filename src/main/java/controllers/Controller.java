package controllers;

import models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller implements Initializable {

    @FXML
    private Label NAME;

    @FXML
    private Button btnlivreur;

    @FXML
    private ImageView IMAGE;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;
    @FXML
    private Button btnEvent;
    @FXML
    private Button btnActivity;

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
    private Button btnSettings1;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Patient currentPatient = SessionManager.getCurrentSession();

        if (currentPatient != null) {
            NAME.setText(currentPatient.getFirstname() + " " + currentPatient.getLastname());
            String imgPath = currentPatient.getImg_path();
            Image image = new Image(imgPath);
            IMAGE.setImage(image);
        }

    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnOverview) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (actionEvent.getSource() == btnOrders) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduitFXML.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (actionEvent.getSource() == btnCustomers) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategorieFXML.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (actionEvent.getSource() == btnMenus) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPharmacieFXML.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (actionEvent.getSource() == btnPackages) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrdonnanceFXML.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (actionEvent.getSource() == btnSettings) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Rendezvous.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (actionEvent.getSource() == btnSettings1) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Rapport.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (actionEvent.getSource() == btnlivreur) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterlivreur.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (actionEvent.getSource() == btnEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (actionEvent.getSource() == btnActivity) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Activity.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    @FXML
    void LOGOUT(MouseEvent event) {
        try {
            SessionManager.endSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene loginScene = new Scene(root);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(loginScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

