package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Activity {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty nom = new SimpleStringProperty(this, "nom");
    private final StringProperty status = new SimpleStringProperty(this, "status");
    private final StringProperty description = new SimpleStringProperty(this, "description");
    private final StringProperty organisateur = new SimpleStringProperty(this, "organisateur");
    private final IntegerProperty evenementId = new SimpleIntegerProperty(this, "evenementId");
    private final StringProperty eventName = new SimpleStringProperty(this, "eventName");

    public String getEventName() {
        return eventName.get();
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public StringProperty eventNameProperty() {
        return eventName;
    }

    // Default constructor
    public Activity() {
    }

    // Constructor with all properties
    public Activity(String nom, String status, String description, String organisateur, int evenementId) {
        this.nom.set(nom);
        this.status.set(status);
        this.description.set(description);
        this.organisateur.set(organisateur);
        this.evenementId.set(evenementId);
    }

    // Getters and setters
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getOrganisateur() {
        return organisateur.get();
    }

    public StringProperty organisateurProperty() {
        return organisateur;
    }

    public void setOrganisateur(String organisateur) {
        this.organisateur.set(organisateur);
    }

    public int getEvenementId() {
        return evenementId.get();
    }

    public IntegerProperty evenementIdProperty() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId.set(evenementId);
    }

}
