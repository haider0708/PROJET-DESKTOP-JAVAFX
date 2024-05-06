package models;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private static Panier instance;
    private List<Produit> produits;

    private Panier() {
        produits = new ArrayList<>();
    }

    public static Panier getInstance() {
        if (instance == null) {
            instance = new Panier();
        }
        return instance;
    }

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }

    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void viderPanier() {
        produits.clear();
    }

    public int getTotal() {
        int total = 0;
        for (Produit produit : produits) {
            total += produit.getPrix();
        }
        return total;
    }
}
