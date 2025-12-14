# Notes Service

Microservice de gestion des notes médicales pour MédiLabo Solutions.

## Description

Ce microservice permet aux médecins de saisir et gérer leurs notes sur les patients.

## Technologies

- Spring Boot 3.5.7
- Spring Data MongoDB
- Spring Security
- MongoDB (NoSQL)

## Configuration

Le service utilise MongoDB comme base de données NoSQL. 

### Prérequis

- Java 17+
- Maven 3.6+
- MongoDB 4.4+ installé et en cours d'exécution

### Configuration MongoDB

Par défaut, l'application se connecte à :
- **Host** : localhost
- **Port** : 27017
- **Database** : medilabo_notes

Vous pouvez modifier ces paramètres dans `src/main/resources/application.properties`.

## Lancement

```bash
mvn spring-boot:run
```

Le service sera accessible sur le port **9091** par défaut.

## Sécurité

Le service utilise Spring Security avec authentification Basic :

| Utilisateur | Mot de passe | Rôles |
|-------------|--------------|-------|
| `user` | `password` | USER |
| `admin` | `admin` | ADMIN, USER |

## Développement

### Structure du projet

```
notes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/medilabo/notes/
│   │   │       ├── config/         # Configuration (MongoDB, Security)
│   │   │       └── NotesApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```