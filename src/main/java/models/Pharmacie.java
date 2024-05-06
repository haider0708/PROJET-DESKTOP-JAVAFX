package models;

import java.util.ArrayList;
import java.util.List;

public class Pharmacie {
    private int id;
    private String nom;
    private String adresse;
    private int numerotelephone;

    private String img;

    // Constructeur
    public Pharmacie() {
    }


    public Pharmacie( String nom, String adresse, int telephone) {
        this.nom = nom;
        this.adresse = adresse;
        this.numerotelephone = telephone;

    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public int getNumeroTelephone() {return numerotelephone;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getImg() {return img;
    }


    public void setNumerotelephone(int numerotelephone) {
        this.numerotelephone = numerotelephone;
    }
    @Override
    public String toString() {
        return "Pharmacie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numero de telephone=" + numerotelephone +
                '}';
    }



}

