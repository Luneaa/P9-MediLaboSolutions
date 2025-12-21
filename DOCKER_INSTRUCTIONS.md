# Guide d'utilisation Docker pour MédiLabo

Ce projet a été conteneurisé pour faciliter le déploiement et les tests. Voici comment utiliser Docker pour lancer l'application.

## Prérequis

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installé et lancé.

## Architecture des Conteneurs

Le fichier `docker-compose.yml` orchestre les services suivants :

1.  **Bases de données :**
    -   `mysql` : Base de données pour le service Patient (Port externe: 3307).
    -   `mongo` : Base de données pour le service Notes (Port externe: 27018).

2.  **Microservices :**
    -   `patient-service` : Gestion des patients (Port: 9090).
    -   `notes-service` : Gestion des notes (Port: 9091).
    -   `evaluation-service` : Évaluation des risques (Port: 9092).
    -   `gateway-service` : Point d'entrée unique (Port: 8080).
    -   `frontend-service` : Interface utilisateur (Port: 8081).

## Lancer l'application

1.  Ouvrez un terminal à la racine du projet (où se trouve le fichier `docker-compose.yml`).

2.  Construisez et lancez les conteneurs :

    ```bash
    docker-compose up --build
    ```

    *L'option `--build` force la reconstruction des images, ce qui est utile si vous avez modifié le code source.*

3.  Attendez que tous les services soient démarrés. Vous verrez des logs défiler.

## Accéder à l'application

Une fois les services démarrés :

-   **Frontend :** [http://localhost:8081](http://localhost:8081)

## Arrêter l'application

Pour arrêter les conteneurs, faites `Ctrl+C` dans le terminal, ou exécutez dans un autre terminal :

```bash
docker-compose down
```

Pour arrêter et supprimer également les volumes de données (réinitialiser les bases de données) :

```bash
docker-compose down -v
```

## Configuration

Les configurations spécifiques à Docker sont injectées via des variables d'environnement dans le fichier `docker-compose.yml`.

-   Les services communiquent entre eux via le réseau Docker interne `medilabo-network`.
-   Les ports des bases de données ont été mappés sur 3307 (MySQL) et 27018 (Mongo) pour éviter les conflits si vous avez déjà ces bases de données installées localement sur les ports par défaut.
