package models;

public class Produit {
    int id;
    private String nom;
    private float prix;
    private String description;
    private int categorie_id;
    private String nomcategorie;
    private String img;



    // Constructeur
    public Produit() {}
    public Produit(String nom, float prix, String description,int categorie_id,String nomcategorie,String img) {
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.categorie_id = categorie_id;
        this.nomcategorie = nomcategorie;
        this.img = img;

    }

    // Méthodes d'accès (getters) et de modification (setters)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;

    }

    public String getNomcategorie() {
        return nomcategorie;
    }

    public void setNomcategorie(String nomcategorie) {
        this.nomcategorie = nomcategorie;
    }
    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description+ '\'' +
                ", prix=" + prix +'\''  +
                ", categorie_id=" + categorie_id +'\''  +
                ", nomcategorie=" + nomcategorie +'\''  +
                ", img=" + img +'\''  +

                '}';
    }
}
