package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty titre = new SimpleStringProperty();
    private final StringProperty localisation = new SimpleStringProperty();
    private final StringProperty date = new SimpleStringProperty();

    public Event() {}


    public Event(String titre, String localisation, String date) {
        this.titre.set(titre);
        this.localisation.set(localisation);
        this.date.set(date);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty titreProperty() {
        return titre;
    }

    public StringProperty localisationProperty() {
        return localisation;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public String getLocalisation() {
        return localisation.get();
    }

    public void setLocalisation(String localisation) {
        this.localisation.set(localisation);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }
    @Override
    public String toString() {
        return getTitre(); // This will allow ComboBox to show the title
    }

}
