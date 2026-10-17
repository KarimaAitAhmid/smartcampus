package ma.ensa.smartcampus.entity;

import ma.ensa.smartcampus.enums.SourceOccupation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

@Document(collection = "occupations_pedagogiques")
public class OccupationPedagogique {

    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String salleId;

    private Instant debut;
    private Instant fin;
    private String libelle;
    private SourceOccupation source = SourceOccupation.MANUEL;
    private Instant dateCreation = Instant.now();

    public OccupationPedagogique() {
    }

    public String getId() { return id; }
    public String getSalleId() { return salleId; }
    public Instant getDebut() { return debut; }
    public Instant getFin() { return fin; }
    public String getLibelle() { return libelle; }
    public SourceOccupation getSource() { return source; }
    public Instant getDateCreation() { return dateCreation; }

    public void setSalleId(String salleId) { this.salleId = salleId; }
    public void setDebut(Instant debut) { this.debut = debut; }
    public void setFin(Instant fin) { this.fin = fin; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public void setSource(SourceOccupation source) { this.source = source; }
}