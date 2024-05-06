package services;

import models.Ordonnance;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrdonnance implements CRUDORD<Ordonnance> {
    private Connection cnx;

    public ServiceOrdonnance() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Ordonnance ordonnance) throws SQLException {
        String req = "INSERT INTO `ordonnance`(`nommedecin`, `nompatient`, `description`, `pharmacie_id`, `nompharmacie`) VALUES (?, ?, ?, ?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, ordonnance.getNommedecin());
        ps.setString(2, ordonnance.getNompatient());
        ps.setString(3, ordonnance.getDescription());
        ps.setInt(4,ordonnance.getPharmacieid());
        ps.setString(5, ordonnance.getNompharmacie());
        // java.sql.Date sqlDate = java.sql.Date.valueOf(ordonnance.getDatecreation());

        // Utiliser sqlDate comme argument pour setDate()
        //ps.setDate(4, sqlDate);
        // ps.setDate(4, ordonnance.getDatecreation());

        ps.executeUpdate();
        System.out.println("Ordonnance ajoutée !");
    }

    @Override
    public void updateOne(Ordonnance ordonnance) throws SQLException {
        String req = "UPDATE `ordonnance` SET `nompatient`=?, `description`=?, `datecreation`=? WHERE `nommedecin`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, ordonnance.getNompatient());
        ps.setString(2, ordonnance.getDescription());
        /*java.sql.Date sqlDate = java.sql.Date.valueOf(ordonnance.getDatecreation());

        // Utiliser sqlDate comme argument pour setDate()
        ps.setDate(3, sqlDate);*/
        ps.setString(3, ordonnance.getNommedecin());

        ps.executeUpdate();
        System.out.println("Ordonnance mise à jour !");
    }

    @Override
    public void deleteOne(Ordonnance ordonnance) throws SQLException, SQLException {
        String req = "DELETE FROM `ordonnance` WHERE `nommedecin`=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, ordonnance.getNommedecin());
        ps.executeUpdate();
        System.out.println("Ordonnance supprimée !");
    }

    @Override
    public List<Ordonnance> selectAll() throws SQLException {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String req = "SELECT * FROM `ordonnance`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setNommedecin(rs.getString("nommedecin"));
            ordonnance.setNompatient(rs.getString("nompatient"));
            ordonnance.setDescription(rs.getString("description"));
            ordonnance.setPharmacie_id(rs.getInt("pharmacie_id"));
            ordonnance.setNompharmacie(rs.getString("nompharmacie"));
            // ordonnance.setDatecreation(rs.getDate("datecreation").toLocalDate());
            ordonnances.add(ordonnance);
        }

        return ordonnances;
    }


}
