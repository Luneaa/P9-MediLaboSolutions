# MédiLabo Solutions

Système de gestion des dossiers patients - Architecture microservices

## 📋 Description

MédiLabo Solutions est une application de gestion des dossiers patients construite avec une architecture microservices. Le système permet de gérer les informations des patients de manière sécurisée et efficace.

## 🏗️ Architecture

Le projet est composé de plusieurs modules :

### 🔹 `patient`
Microservice de gestion des données patients
- **Technologie** : Spring Boot 3.5.7
- **Base de données** : MySQL
- **Sécurité** : Spring Security avec authentification Basic
- **API REST** : Endpoints pour CRUD des patients
- **Port** : 9090 (par défaut)

### 🔹 `notes`
Microservice de gestion des notes médicales
- **Technologie** : Spring Boot 3.5.7
- **Base de données** : MongoDB (NoSQL)
- **Sécurité** : Spring Security avec authentification Basic
- **API REST** : Endpoints pour la gestion des notes des médecins
- **Port** : 9091 (par défaut)

### 🔹 `gateway`
Gateway API donnant accès aux microservices
- **Technologie** : Spring Cloud Gateway
- **Rôle** : Point d'entrée unique pour tous les microservices
- **Routage** : Redirection des requêtes vers les services appropriés
- **Port** : 8080 (par défaut)

### 🔹 `frontend`
Application web frontend
- **Technologie** : Spring Boot avec Thymeleaf
- **Framework CSS** : Tailwind CSS
- **Icônes** : Tabler Icons
- **Sécurité** : Spring Security avec authentification par formulaire
- **Features** :
  - Authentification utilisateur
  - Liste des patients
  - Interface responsive
- **Port** : 8081 (par défaut)

### 🔹 `bruno-api`
Collection de requêtes API pour tests
- **Outil** : Bruno (client API)
- **Contenu** : Exemples de requêtes HTTP pour tester les endpoints
- **Organisation** : Requêtes organisées par service et environnement

## 🚀 Démarrage rapide

### Prérequis
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- MongoDB 4.4+
- Node.js (pour Tailwind CSS)

### Lancement des services

1. **Démarrer MongoDB**
   Assurez-vous que MongoDB est en cours d'exécution sur le port 27017

2. **Démarrer le microservice Patient**
   ```bash
   cd patient
   mvn spring-boot:run
   ```

3. **Démarrer le microservice Notes**
   ```bash
   cd notes
   mvn spring-boot:run
   ```

4. **Démarrer la Gateway**
   ```bash
   cd gateway
   mvn spring-boot:run
   ```

5. **Démarrer le Frontend**
   ```bash
   cd frontend
   mvn spring-boot:run
   ```

6. **Accéder à l'application**
   - Frontend : http://localhost:8081
   - Gateway : http://localhost:8080
   - Service Patient (direct) : http://localhost:9090
   - Service Notes (direct) : http://localhost:9091

## 🔐 Authentification

### Utilisateurs par défaut

Le système utilise une authentification en mémoire avec les comptes suivants :

| Utilisateur | Mot de passe | Rôles |
|-------------|--------------|-------|
| `user` | `password` | USER |
| `admin` | `admin` | ADMIN, USER |

## 🛠️ Technologies utilisées

- **Backend** : Spring Boot 3.5.7, Spring Security, Spring Cloud Gateway
- **Frontend** : Thymeleaf, Tailwind CSS, Tabler Icons
- **Bases de données** : MySQL (patients), MongoDB (notes médicales)
- **Build** : Maven
- **API Testing** : Bruno

## 📁 Structure du projet

```
P9/
├── patient/           # Microservice de gestion des patients (MySQL)
├── notes/             # Microservice de gestion des notes médicales (MongoDB)
├── gateway/           # Gateway API
├── frontend/          # Application web frontend
├── bruno-api/         # Collections de requêtes API
└── README.md          # Ce fichier
```

## 🔄 Architecture de communication

```
Client Browser
    ↓
Frontend (Port 8081)
    ↓
Gateway (Port 8080)
    ↓
    ├─→ Patient Service (Port 9090) → MySQL Database
    └─→ Notes Service (Port 9091) → MongoDB Database
```

## 📝 Licence

Copyright © 2025 MédiLabo Solutions - Tous droits réservés

## 👥 Contribution

Ce projet est développé dans le cadre d'une formation OpenClassrooms.
