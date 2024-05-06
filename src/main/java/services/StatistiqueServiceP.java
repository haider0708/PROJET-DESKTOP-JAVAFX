package services;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StatistiqueServiceP {
    private Connection cnx;
    public StatistiqueServiceP() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public Map<String, Double> getPourcentageOrdonnancesParPharmacie() throws SQLException {
        Map<String, Integer> nombreOrdonnancesParPharmacie = new HashMap<>();
        Map<String, Double> pourcentageOrdonnancesParPharmacie = new HashMap<>();

        // Requête SQL pour récupérer le nombre d'ordonnances par pharmacie
        String sql = "SELECT nompharmacie, COUNT(*) AS nombre_ordonnances FROM ordonnance GROUP BY nompharmacie";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            // Calcul du nombre total d'ordonnances et remplissage de nombreOrdonnancesParPharmacie
            int totalOrdonnances = 0;
            while (resultSet.next()) {
                String nomPharmacie = resultSet.getString("nompharmacie");
                int nombreOrdonnances = resultSet.getInt("nombre_ordonnances");
                nombreOrdonnancesParPharmacie.put(nomPharmacie, nombreOrdonnances);
                totalOrdonnances += nombreOrdonnances;
            }

            // Calcul des pourcentages pour chaque pharmacie
            for (Map.Entry<String, Integer> entry : nombreOrdonnancesParPharmacie.entrySet()) {
                String nomPharmacie = entry.getKey();
                int nombreOrdonnances = entry.getValue();
                double pourcentage = (double) nombreOrdonnances / totalOrdonnances * 100;
                pourcentageOrdonnancesParPharmacie.put(nomPharmacie, pourcentage);
            }
        }

        return pourcentageOrdonnancesParPharmacie;
    }

}