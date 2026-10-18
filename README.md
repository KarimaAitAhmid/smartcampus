# SmartCampus

Application web de gestion et de réservation des salles universitaires (ENSA Khouribga).

## Backend

- Java 21, Spring Boot
- MongoDB Atlas
- Spring Security + JWT
- Tests : JUnit 5, Mockito, AssertJ

Lancer les tests :

    cd backend
    ./mvnw test

## Authentification

### Connexion - POST /api/auth/login

L'utilisateur envoie son e-mail et son mot de passe. Si les identifiants sont corrects, l'API renvoie un jeton JWT.
Après trois échecs consécutifs, le compte est bloqué pendant 15 minutes.

### Déconnexion - POST /api/auth/logout (ticket SC-11)

- La route est protégée : il faut envoyer le jeton dans l'en-tête `Authorization: Bearer <token>`.
- Sans jeton valide, l'API répond `401 NON_AUTHENTIFIE`.
- Avec un jeton valide, l'API répond `204 No Content`.
- Le client (frontend) doit ensuite supprimer le jeton qu'il a stocké. Les pages protégées demandent alors une nouvelle connexion.

#### Limite connue

La déconnexion côté client ne révoque pas automatiquement le jeton JWT côté serveur : l'application est sans état et ne conserve pas de liste de jetons révoqués.
Un jeton copié avant la déconnexion reste donc valide jusqu'à son expiration (`app.jwt.expiration-ms`, soit 1 heure).
Une révocation côté serveur (liste noire de jetons, ou jetons courts avec jeton de rafraîchissement) pourra être ajoutée dans un prochain sprint.
