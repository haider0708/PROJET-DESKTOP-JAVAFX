package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.Categorie;
import models.Produit;
import utils.DBConnection;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
public class CategorieFXML implements Initializable {
    Connection cnx =null;
    PreparedStatement st =null;
    ResultSet rs = null;
    @FXML
    private TextField filtrer;


    @FXML
    private Button btnAjouterc;

    @FXML
    private Button btnClearc;

    @FXML
    private Button btnModifierc;

    @FXML
    private Button btnSupprimerc;
    @FXML
    private TextField tfnom;
    @FXML
    private TableColumn<Categorie, String > colnom;

    @FXML
    private TableView<Categorie> tablec;
    @FXML
    private TableColumn<Categorie, Integer> colId;

    @FXML
    void Clearc(ActionEvent event) {
        clear();
    }

    @FXML
    void Modifierc(ActionEvent event) throws SQLException {
        String update = "update categorie set nomcategorie = ? where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try{
            PreparedStatement categorie = cnx.prepareStatement(update);
            categorie.setString(1,tfnom.getText());
            categorie.setInt(2,id);
            categorie.executeUpdate();
            showCategorie();
            clear();
            showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "La catégorie a été modifiée avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void Supprimerc(ActionEvent event) throws SQLException {
        String delete ="delete from categorie where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try {
            PreparedStatement categorie = cnx.prepareStatement(delete);
            categorie.setInt(1,id);
            categorie.executeUpdate();
            showCategorie();
            clear();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "La catégorie a été supprimée avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @Override
    public void initialize(URL url , ResourceBundle resourceBundle){
        cnx = DBConnection.getInstance().getCnx();
        showCategorie();

        // Activer le tri sur la TableView
        tablec.setSortPolicy(param -> {
            ObservableList<Categorie> items = tablec.getItems();
            items.sort((o1, o2) -> {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                // Modifier cette partie selon la colonne sur laquelle vous voulez trier
                return o1.getNomcategorie().compareTo(o2.getNomcategorie());
            });
            return true;
        });

        // Ajouter le gestionnaire d'événements pour le filtrage
        filtrer.setOnKeyReleased(this::filtrerTexte);
    }
    @FXML
    void filtrerTexte(KeyEvent event) {
        String filtre = filtrer.getText().trim();
        ObservableList<Categorie> categoriesFiltrees = filterCategories(filtre);
        tablec.setItems(categoriesFiltrees);
    }
    private ObservableList<Categorie> filterCategories(String filterText) {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        for (Categorie categorie : getcategories()) {
            if (categorie.getNomcategorie().toLowerCase().contains(filterText.toLowerCase())) {
                categories.add(categorie);
            }
        }
        return categories;
    }




    public int id = 0;
    public ObservableList<Categorie> getcategories() {
        ObservableList<Categorie> categories= FXCollections.observableArrayList();
        String query = "select* from categorie ";
        cnx = DBConnection.getInstance().getCnx();

        try {
            st = cnx.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
               Categorie categorie = new Categorie();
                categorie.setNom(rs.getString("NomCategorie"));
                categorie.setId(rs.getInt("Id"));
                categories.add(categorie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
    public void showCategorie()
    {
        ObservableList<Categorie> list = getcategories();
        tablec.setItems(list);
        //colId.setCellValueFactory(new PropertyValueFactory<Categorie,Integer>("Id"));
        colnom.setCellValueFactory(new PropertyValueFactory<Categorie,String>("nomcategorie"));



    }
    @FXML
    void Ajouterc(ActionEvent event) {
        String nomcategorie = tfnom.getText();

        // Valider le nom de la catégorie
        if (!validateInput(nomcategorie)) {
            // Sortir de la méthode si les données ne sont pas valides
            return;
        }
        String insert = "INSERT INTO categorie(nomcategorie) VALUES (?)";
        Connection cnx = DBConnection.getInstance().getCnx();
        try {
            PreparedStatement categorie = cnx.prepareStatement(insert);
            categorie.setString(1, tfnom.getText());
            categorie.executeUpdate();
            showCategorie();
            showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "La catégorie a été ajoutée avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void getDatac(MouseEvent event) {

        Categorie categorie= tablec.getSelectionModel().getSelectedItem();
        id = categorie.getId();
        tfnom.setText(categorie.getNomcategorie());
        btnAjouterc.setDisable(true);

    }
    void clear(){
        tfnom.setText(null);
        btnAjouterc.setDisable(false);
    }

    private boolean validateInput(String nomcategorie) {
        // Vérifier si le nom de la catégorie est vide ou null
        if (nomcategorie == null || nomcategorie.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ de nom de catégorie est vide.");
            return false;
        }

        // Vérifier si le nom de la catégorie contient des chiffres ou des symboles
        if (!nomcategorie.matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom de la catégorie ne doit contenir que des lettres et des espaces.");
            return false;
        }

        // Vérifier si le nom de la catégorie dépasse les 3 caractères
        if (nomcategorie.length() <= 3) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom de la catégorie doit contenir plus de 3 caractères.");
            return false;
        }

        // Retourner true si toutes les conditions de validation sont satisfaites
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}