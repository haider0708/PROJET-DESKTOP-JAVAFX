package models;
import java.sql.Date;
import java.time.LocalDate;

public class Ordonnance {
    private int id;
    private String nommedecin;
    private String nompatient;
    private String description;
    private  int pharmacie_id;

    private String nompharmacie;


    // Constructeur
    public Ordonnance(){
    }
    public Ordonnance(String nommedecin, String nompatient, String description, int pharmacie_id,String nompharmacie) {
        this.nommedecin = nommedecin;
        this.nompatient = nompatient;
        this.description = description;
        this.pharmacie_id=pharmacie_id;
        this.nompharmacie=nompharmacie;

    }

    // Getters et setters
    public int getId(){return id;}
    public void setId(int id){
        this.id=id;
    }

    public String getNommedecin() {
        return nommedecin;
    }

    public void setNommedecin(String nommedecin) {
        this.nommedecin = nommedecin;
    }

    public String getNompatient() {
        return nompatient;
    }

    public void setNompatient(String nompatient) {
        this.nompatient = nompatient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getPharmacieid() {
        return pharmacie_id;
    }

    public void setPharmacie_id(int pharmacie_id) {
        this.pharmacie_id = pharmacie_id;
    }
    public String getNompharmacie() {
        return nompharmacie;
    }

    public void setNompharmacie(String nompharmacie) {
        this.nompharmacie = nompharmacie;
    }

    @Override
    public String toString() {
        return "Ordonnance{"+
                "id=" + id +
                "nommedecin='" + nommedecin + '\'' +
                ", nompatient='" + nompatient + '\'' +
                ", description='" + description + '\'' +
                ",pharmacie id='"+pharmacie_id+
                ",nompharmacie '"+nompharmacie+

                '}';
    }


}
