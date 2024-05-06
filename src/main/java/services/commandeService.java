/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package services;

import models.commande;
import models.livreur;
import models.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import utils.MyDB;

//**************//


/**
 *
 * @author asus
 */
public class commandeService {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public commandeService() {

        cnx = MyDB.getInstance().getCnx();
    }

    public void ajoutercommande(commande p) {
        user U = new user();
        livreurService es = new livreurService();
        String requete = "INSERT INTO `commande` (`id` ,`nom_client`,`addresse_client`,`numero_client`,`rating`)  VALUES(?,?,?,?,?) ;";

        try {
            livreur tempev = es.FetchOneev(p.getId());
            System.out.println("before" + tempev);

            es.modifierlivreur(tempev);
            int new_id = tempev.getId();
            p.setLivreur(tempev);
            System.out.println("after" + tempev);

            pst = (PreparedStatement) cnx.prepareStatement(requete);

            pst.setInt(1, p.getId());

            pst.setString(2, p.getNom_client());
            pst.setString(3, p.getAddresse_client());
            pst.setString(4, p.getNumero_client());
            pst.setDouble(5, p.getRating());



            pst.executeUpdate();
          

            System.out.println("commande with id ev = " + p.getId() + " is added successfully");

        } catch (SQLException ex) {
            System.out.println("error in adding commande");
            Logger.getLogger(commandeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<commande> recupererCommande() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temlivreures.
        commande dernierNom_client = null;

        List<commande> particip = new ArrayList<>();
        String s = "SELECT * FROM commande WHERE id_commande = (SELECT MAX(id_commande) FROM commande)";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            commande pa = new commande();
            pa.setId_commande(rs.getInt("id_commande"));

            pa.setId(rs.getInt("id"));
            pa.setNom_client(rs.getString("nom_client"));
            pa.setAddresse_client(rs.getString("addresse_client"));
            pa.setNumero_client(rs.getString("numero_client"));


            particip.add(pa);

        }
        return particip;
    }
    public List<commande> recupererComment() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temlivreures.
        commande dernierNom_client = null;
        List<commande> particip = new ArrayList<>();
        String s = "select * from commande";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            commande pa = new commande();
            pa.setId_commande(rs.getInt("id_commande"));

            pa.setId(rs.getInt("id"));

            pa.setNom_client(rs.getString("nom_client"));
            pa.setAddresse_client(rs.getString("addresse_client"));
            pa.setNumero_client(rs.getString("numero_client"));

            particip.add(pa);

        }
        return particip;
    }



    public commande FetchOneRes(int id) throws SQLException {
        commande r = new commande();
        String requete = "SELECT * FROM `commande` where id_commande=" + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                r = new commande(rs.getInt("id_commande"),  rs.getInt("id"), rs.getString("nom_client"), rs.getString("addresse_client"), rs.getString("numero_client"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(livreurService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public void Deletecommande(commande p) throws SQLException {
        livreurService es = new livreurService();
        commandeService rs = new commandeService();

        commande r = rs.FetchOneRes(p.getId_commande());

        String requete = "delete from commande where id_commande=" + p.getId_commande();
        try {
            livreur tempev = es.FetchOneev(r.getId());
            System.out.println("before" + tempev);

            es.modifierlivreur(tempev);
            System.out.println("after" + tempev);
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            //pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("commande with id=" + p.getId_commande() + " is deleted successfully");
        } catch (SQLException ex) {
            System.out.println("error in delete commande " + ex.getMessage());
        }
    }
    
    public void modifiercommande(commande p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temlivreures.
        String req = "UPDATE commande SET id = ?,nom_client = ?,addresse_client = ?,numero_client = ? where id_commande = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, p.getId());

        ps.setString(2, p.getNom_client());
        ps.setString(3, p.getAddresse_client());
        ps.setString(4, p.getNumero_client());

        ps.setInt(5, p.getId_commande());


        ps.executeUpdate();
    }
    public Map<Integer, Integer> countCommandesParLivreur() throws SQLException {
        Map<Integer, Integer> commandesParLivreur = new HashMap<>();

        // Récupérer toutes les commandes
        List<commande> commandes = recupererComment();

        // Parcourir les commandes pour compter le nombre de commandes par livreur
        for (commande cmd : commandes) {
            int idLivreur = cmd.getId();
            // Si l'ID du livreur est déjà dans la map, incrémenter le compteur
            if (commandesParLivreur.containsKey(idLivreur)) {
                int count = commandesParLivreur.get(idLivreur);
                commandesParLivreur.put(idLivreur, count + 1);
            } else {
                // Sinon, initialiser le compteur à 1
                commandesParLivreur.put(idLivreur, 1);
            }
        }

        return commandesParLivreur;
    }

}
