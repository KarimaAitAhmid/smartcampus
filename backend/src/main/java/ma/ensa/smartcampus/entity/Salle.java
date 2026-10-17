package ma.ensa.smartcampus.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "salles")
public class Salle {

    @Id
    private String id;

    private String nom;
    private String batiment;
    private int capacite;
    private String description;
    private List<String> equipements = new ArrayList<>();
    private boolean active = true;
    private Instant dateCreation = Instant.now();

    public Salle() {
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getBatiment() { return batiment; }
    public int getCapacite() { return capacite; }
    public String getDescription() { return description; }
    public List<String> getEquipements() { return equipements; }
    public boolean isActive() { return active; }
    public Instant getDateCreation() { return dateCreation; }

    public void setNom(String nom) { this.nom = nom; }
    public void setBatiment(String batiment) { this.batiment = batiment; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setDescription(String description) { this.description = description; }
    public void setEquipements(List<String> equipements) { this.equipements = equipements; }
    public void setActive(boolean active) { this.active = active; }
}