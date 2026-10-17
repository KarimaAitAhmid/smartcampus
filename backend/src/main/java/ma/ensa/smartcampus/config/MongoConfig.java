package ma.ensa.smartcampus.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    public MongoConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialiserCollections() {
        creerCollectionSiAbsente("utilisateurs");
        creerCollectionSiAbsente("salles");
        creerCollectionSiAbsente("reservations");
        creerCollectionSiAbsente("occupations_pedagogiques");

        System.out.println(
                "Initialisation des collections SmartCampus terminée."
        );
    }

    private void creerCollectionSiAbsente(String nom) {
        if (!mongoTemplate.collectionExists(nom)) {
            mongoTemplate.createCollection(nom);
            System.out.println("Collection créée : " + nom);
        } else {
            System.out.println("Collection déjà existante : " + nom);
        }
    }
}
