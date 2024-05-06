package models;

/**
 *
 * @author asus
 */
public class livreur {

    private int id;
    private String  image, nom, prenom;
    private int numero_tel;



    public livreur() {
    }

    public livreur(int id, String image, String nom, String prenom, int numero_tel) {
        this.id = id;

        this.image = image;
        this.nom = nom;
        this.prenom = prenom;
        this.numero_tel = numero_tel;

    }

    public livreur(String image, String nom, String prenom, int numero_tel) {

        this.image = image;
        this.nom = nom;
        this.prenom = prenom;
        this.numero_tel = numero_tel;

    }



    //****************** getters ****************

    public int getId() {
        return id;
    }



    public String getImage() {
        return image;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getNumero_tel() {
        return numero_tel;
    }



    //****************** setters ****************

    public void setId(int id) {
        this.id = id;
    }



    public void setImage(String image) {
        this.image = image;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNumero_tel(int numero_tel) {
        this.numero_tel = numero_tel;
    }



    @Override
    public String toString() {
        return "livreur{" + "id=" + id +  ", image=" + image
                + ", nom=" + nom + ", prenom=" + prenom + ", numero_tel=" + numero_tel
                +  '}';
    }
}
