package services;

import models.livreur;
import utils.MyDB;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

//**************//
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;


public class livreurService implements IlivreurService<livreur> {

    Connection cnx;

    public PreparedStatement pst;

    public livreurService() {
        cnx = MyDB.getInstance().getCnx();
    }

    @Override
    public void ajouterlivreur(livreur e) throws SQLException {
        String requete = "INSERT INTO `livreur` (`image`,`nom`,`prenom`, `numero_tel`) "
                + "VALUES (?,?,?,?);";
        try {
            pst = cnx.prepareStatement(requete);

            pst.setString(1, e.getImage());
            pst.setString(2, e.getNom());
            pst.setString(3, e.getPrenom());
            pst.setInt(4, e.getNumero_tel());


            pst.executeUpdate();
            System.out.println("livreur " +  " ajouté avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifierlivreur(livreur e) throws SQLException {
        String req = "UPDATE livreur SET image=?,nom = ?,prenom = ?, numero_tel=? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, e.getImage());
        ps.setString(2, e.getNom());
        ps.setString(3, e.getPrenom());

        ps.setInt(4, e.getNumero_tel());

        ps.setInt(5, e.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimerlivreur(livreur e) throws SQLException {
        String req = "DELETE FROM livreur WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, e.getId());
        ps.executeUpdate();
        System.out.println("livreur avec l'id= " + e.getId() + " supprimé avec succès");
    }

    @Override
    public List<livreur> recupererlivreur() throws SQLException {
        List<livreur> livreur = new ArrayList<>();
        String s = "SELECT * FROM livreur";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            livreur e = new livreur();

            e.setImage(rs.getString("Image"));
            e.setNom(rs.getString("nom"));
            e.setPrenom(rs.getString("prenom"));

            e.setNumero_tel(rs.getInt("numero_tel"));

            e.setId(rs.getInt("id"));
            livreur.add(e);
        }
        return livreur;
    }

    public livreur FetchOneev(int id) {
        livreur ev = new livreur();
        String requete = "SELECT * FROM `livreur` where id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ev = new livreur(rs.getInt("id"),rs.getString("image"),rs.getString("nom"), rs.getString("prenom"), rs.getInt("numero_tel") );
            }
        } catch (SQLException ex) {
            Logger.getLogger(livreurService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }



    public ObservableList<livreur> chercherev(String chaine) {
        String sql = "SELECT * FROM livreur WHERE (nom LIKE ? or numero_tel LIKE ?  ) order by nom ";
        String ch = "%" + chaine + "%";
        ObservableList<livreur> myList = FXCollections.observableArrayList();
        try {
            PreparedStatement stee = cnx.prepareStatement(sql);
            stee.setString(1, ch);
            stee.setString(2, ch);

            ResultSet rs = stee.executeQuery();
            while (rs.next()) {
                livreur e = new livreur();


                e.setImage(rs.getString("Image"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));

                e.setId(rs.getInt("id"));
                e.setNumero_tel(rs.getInt("numero_tel"));

                myList.add(e);
                System.out.println("livreur trouvé! ");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public List<livreur> trierev() throws SQLException {
        List<livreur> livreur = new ArrayList<>();
        String s = "select * from livreur order by nom ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            livreur e = new livreur();

            e.setImage(rs.getString("Image"));
            e.setNom(rs.getString("nom"));
            e.setPrenom(rs.getString("prenom"));

            e.setId(rs.getInt("id"));
            e.setNumero_tel(rs.getInt("numero_tel"));

            livreur.add(e);
        }
        return livreur;
    }


    public String GenerateQrev(livreur ev) throws FileNotFoundException, IOException {
        String evName = "livreur name: " + ev.getNom() + "\n" + "livreur prenom: " + ev.getPrenom() + "\n" + "livreur numero: " + ev.getNumero_tel() + "\n" +  "\n";
        ByteArrayOutputStream out = QRCode.from(evName).to(ImageType.JPG).stream();
        String filename = ev.getNom() + "_QrCode.jpg";
        //File f = new File("src\\utils\\img\\" + filename);
        File f = new File("C:\\xampp\\htdocs\\imgQr\\qrcode" + filename);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(out.toByteArray());
        fos.flush();

        System.out.println("qr yemshi");
        return filename;
    }
}
