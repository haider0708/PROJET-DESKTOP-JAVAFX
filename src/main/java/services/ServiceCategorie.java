package services;

import models.Categorie;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorie implements CRUDc<Categorie> {

    private Connection cnx;

    public ServiceCategorie() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Categorie categorie) throws SQLException {
        String req = "INSERT INTO categorie (nom) VALUES (?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, categorie.getNomcategorie());
        ps.executeUpdate();
        System.out.println("Categorie ajoutée !");
    }

    @Override
    public void updateOne(Categorie categorie) throws SQLException {
        String req = "UPDATE categorie SET nom = ? WHERE nom = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, categorie.getNomcategorie());
       // ps.setString(2, categorie.getNom()); // Assuming you want to update based on the name
        ps.executeUpdate();
        System.out.println("Categorie mise à jour !");
    }

    @Override
    public void deleteOne(Categorie categorie) throws SQLException {
        String req = "DELETE FROM categorie WHERE nom = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, categorie.getNomcategorie());
        ps.executeUpdate();
        System.out.println("Categorie supprimée !");
    }

    @Override
    public List<Categorie> selectAll() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String req = "SELECT * FROM `categorie`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            // Assuming the Categorie class has a constructor that accepts a String parameter
            Categorie categorie = new Categorie();
            categorie.setNom(rs.getString("NomCategorie"));
            categories.add(categorie);
        }

        return categories;
    }
}