package models;

public class Categorie {
    private int id;
    private String nomcategorie;

    // Constructeur
    public Categorie() {
        // Constructeur par défaut
    }

    public Categorie(int id, String nomcategorie) {
        this.id = id;
        this.nomcategorie = nomcategorie;
    }
    // Setter pour l'ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter pour l'ID
    public int getId() {
        return id;
    }

    // Setter pour le nom
    public String getNomcategorie() {
        return nomcategorie;
    }

    public void setNomcategorie(String nomcategorie) {
        this.nomcategorie = nomcategorie;
    }

    public void setNom(String nomcategorie) {
        this.nomcategorie = nomcategorie;
    }
    // Méthode toString pour afficher les informations de l'objet
    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nomcategorie + '\'' +
                '}';
    }

}
