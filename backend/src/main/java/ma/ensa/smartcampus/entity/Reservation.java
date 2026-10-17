package ma.ensa.smartcampus.entity;

import ma.ensa.smartcampus.enums.StatutReservation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String utilisateurId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String salleId;

    private Instant debut;
    private Instant fin;
    private String motif;
    private StatutReservation statut = StatutReservation.EN_ATTENTE;
    private String commentaireAdmin;
    private Instant dateCreation = Instant.now();

    public Reservation() {
    }

    public String getId() { return id; }
    public String getUtilisateurId() { return utilisateurId; }
    public String getSalleId() { return salleId; }
    public Instant getDebut() { return debut; }
    public Instant getFin() { return fin; }
    public String getMotif() { return motif; }
    public StatutReservation getStatut() { return statut; }
    public String getCommentaireAdmin() { return commentaireAdmin; }
    public Instant getDateCreation() { return dateCreation; }

    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setSalleId(String salleId) { this.salleId = salleId; }
    public void setDebut(Instant debut) { this.debut = debut; }
    public void setFin(Instant fin) { this.fin = fin; }
    public void setMotif(String motif) { this.motif = motif; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }
    public void setCommentaireAdmin(String commentaireAdmin) { this.commentaireAdmin = commentaireAdmin; }
}