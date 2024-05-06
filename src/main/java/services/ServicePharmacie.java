package services;

import models.Pharmacie;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePharmacie implements CRUDP<Pharmacie> {

    private Connection cnx;

    public ServicePharmacie() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Pharmacie pharmacie) throws SQLException {
        String req = "INSERT INTO `pharmacie`(`nom`, `adresse`, `numerotelephone`,`img`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, pharmacie.getNom());
        ps.setString(2, pharmacie.getAdresse());
        ps.setInt(3, pharmacie.getNumeroTelephone());
        ps.setString(4, pharmacie.getImg());

        ps.executeUpdate();
        System.out.println("Pharmacie ajoutée !");
    }

    @Override
    public void updateOne(Pharmacie pharmacie) throws SQLException {
        String req = "UPDATE `pharmacie` SET `adresse`=?, `numerotelephone`=? WHERE `nom`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, pharmacie.getAdresse());
        ps.setInt(2, pharmacie.getNumeroTelephone());
        ps.setString(3, pharmacie.getNom());

        ps.executeUpdate();
        System.out.println("Pharmacie mise à jour !");
    }

    @Override
    public void deleteOne(Pharmacie pharmacie) throws SQLException {
        String req = "DELETE FROM `pharmacie` WHERE `nom`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, pharmacie.getNom());
        ps.executeUpdate();
        System.out.println("Pharmacie supprimée !");
    }


    @Override
    public List<Pharmacie> selectAll() throws SQLException {
        List<Pharmacie> pharmacies = new ArrayList<>();
        String req = "SELECT * FROM `pharmacie`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Pharmacie pharmacie = new Pharmacie();
            pharmacie.setNom(rs.getString("nom"));
            pharmacie.setAdresse(rs.getString("adresse"));
            pharmacie.setNumerotelephone(rs.getInt("numerotelephone"));
            pharmacie.setImg((rs.getString("img")));
            pharmacies.add(pharmacie);
        }

        return pharmacies;
    }


}
