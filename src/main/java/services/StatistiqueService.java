package services;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StatistiqueService {

    private Connection cnx;

    public StatistiqueService() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public Map<String, Integer> getNombreProduitsParCategorie() {
        Map<String, Integer> stats = new HashMap<>();

        try {
            String query = "SELECT nomcategorie, COUNT(*) AS nombre_produits FROM produit GROUP BY nomcategorie";
            try (PreparedStatement statement = cnx.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String nomcategorie = resultSet.getString("nomcategorie");
                    int nombreProduits = resultSet.getInt("nombre_produits");
                    stats.put(nomcategorie, nombreProduits);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }
}
