package ma.ensa.smartcampus.entity;

import ma.ensa.smartcampus.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "utilisateurs")
public class Utilisateur {

    @Id
    private String id;

    private String nom;

    @Indexed(unique = true)
    private String email;

    private String passwordHash;

    private Role role;

    private boolean actif = true;

    private int nbEchecs = 0;

    private Instant bloqueJusqua;

    private Instant dateCreation = Instant.now();

    public Utilisateur() {
    }

    public boolean estBloque(Instant maintenant) {
        return bloqueJusqua != null
                && maintenant.isBefore(bloqueJusqua);
    }

    public void enregistrerEchec(Instant maintenant) {
        nbEchecs++;

        if (nbEchecs >= 3) {
            bloqueJusqua = maintenant.plusSeconds(15 * 60);
        }
    }

    public void reinitialiserEchecs() {
        nbEchecs = 0;
        bloqueJusqua = null;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActif() {
        return actif;
    }

    public int getNbEchecs() {
        return nbEchecs;
    }

    public Instant getBloqueJusqua() {
        return bloqueJusqua;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}