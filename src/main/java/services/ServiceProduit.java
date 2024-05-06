package services;

import models.Produit;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements CRUD<Produit> {

    private Connection cnx;

    public ServiceProduit() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Produit produit) throws SQLException {

        String req = "INSERT INTO produit (nom, prix, description,img) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, produit.getNom());
        ps.setFloat(2, produit.getPrix());
        ps.setString(3, produit.getDescription());
        ps.setString(4, produit.getImg());
        ps.executeUpdate();
        System.out.println("Produit ajouté !");
    }


    @Override
    public void updateOne(Produit produit) throws SQLException {
        String req = "UPDATE produit SET description=?, prix=? WHERE nom=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, produit.getDescription());
        ps.setFloat(2, produit.getPrix());
        ps.setString(3, produit.getNom());

        ps.executeUpdate();
        System.out.println("Produit mis à jour !");
    }


    @Override
    public void deleteOne(Produit produit) throws SQLException {
        String req = "DELETE FROM produit WHERE `nom`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, produit.getNom());
        ps.executeUpdate();
        System.out.println("Produit supprimé !");
    }


    @Override
    public ArrayList<Produit> selectAll() throws SQLException {
        ArrayList<Produit> produits = new ArrayList<>();
        String req = "SELECT * FROM produit";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Produit produit = new Produit();
            produit.setNom(rs.getString("nom"));
            produit.setDescription(rs.getString("description"));
            produit.setPrix(rs.getFloat("prix"));
            produit.setImg(rs.getString("img"));
            produits.add(produit);
        }

        return produits;
    }
}
