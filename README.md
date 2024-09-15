# Projet Événements Sportifs

Ce projet est une plateforme permettant aux utilisateurs de créer, partager et participer à des événements sportifs dans leur région. Les utilisateurs peuvent s'inscrire, rejoindre des événements, laisser des notes et des commentaires sur les événements auxquels ils ont participé.

## Fonctionnalités

1. **Authentification et autorisation** : Les utilisateurs peuvent s'inscrire et se connecter à la plateforme. Les utilisateurs peuvent avoir l'un des rôles suivants : ROLE_USER, ROLE_ADMIN.
2. **Création d'événements sportifs** : Les utilisateurs peuvent créer des événements sportifs en précisant le sport, la date, l'heure, le lieu, le nombre de places disponibles et une description.
3. **Recherche et participation aux événements** : Les utilisateurs peuvent rechercher des événements sportifs en fonction de leur sport préféré, de leur localisation ou de la date. Ils peuvent s'inscrire à des événements en fonction des places disponibles.
4. **Notes et commentaires** : Après avoir participé à un événement, les utilisateurs peuvent laisser une note et un commentaire sur l'événement et l'organisateur.
5. **Géolocalisation et cartographie** : Les événements sont affichés sur une carte interactive, permettant aux utilisateurs de visualiser facilement les événements à proximité de leur localisation.
6. **Espace administrateur** : Les utilisateurs ayant le rôle ROLE_ADMIN ont accès à un espace administrateur leur permettant de gérer les événements, les utilisateurs et les rôles.
7. **CRUD événements et commentaires** : Les utilisateurs ayant le rôle ROLE_USER peuvent créer, lire, mettre à jour et supprimer leurs événements et commentaires respectifs.

## Structure de la base de données

La base de données comprend les tables suivantes :

1. `roles` : stocke les différents rôles d'utilisateur (ROLE_USER et ROLE_ADMIN).
2. `users` : stocke les informations sur les utilisateurs, y compris leurs rôles.
3. `sports` : stocke les différents sports disponibles sur la plateforme.
4. `events` : stocke les informations sur les événements sportifs, y compris l'organisateur, le sport associé, la date, l'heure, la localisation et le nombre de places disponibles.
5. `registrations` : stocke les inscriptions des utilisateurs aux événements sportifs, y compris la date d'inscription.
6. `ratings` : stocke les notes attribuées par les utilisateurs aux événements auxquels ils ont participé, ainsi qu'à l'organisateur de l'événement.
7. `comments` : stocke les commentaires laissés par les utilisateurs pour les événements auxquels ils ont participé.

Les tables sont reliées entre elles à l'aide de clés relationnelles.

## Technologies utilisées

- Java pour le développement de l'API
- ReactJS pour le développement de l'application front-end
- MySQL pour la gestion de la base de données

## Prérequis
- [Nodejs 18.16.0](https://nodejs.org/en/download) Vous pouvez utiliser [Volta](https://volta.sh) pour gérer les versions de NodeJS
- [JDK 11](https://adoptium.net/temurin/releases/?version=11) pour lancer le serveur
- [Mysql 5.7](https://dev.mysql.com/downloads/mysql/5.7.html) pour la base de données

## Configuration
Frontend
- `APP_ENV`: `production` ou `development` defaut: `production`
- `API_URL`: URL de l'API defaut: `http://localhost:8090` (Requis)

Backend
- `server.port`: port du serveur. défaut: `8090`
- `spring.datasource.url`: URL de la base de données. defaut: `jdbc:mysql://localhost:3306/quest_web?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC`
- `spring.datasource.username`: username de la base de données. defaut: `application`
- `spring.datasource.password`: mot de passe de la base de données. defaut: `password`
- `jwt.secret`: secret pour creer les tokens JWT. defaut:`change-me-in-env`
- `jwt.header`: l'en-tête du token JWT. defaut: `Authorization`
- `jwt.prefix`: le préfixe du token JWT. defaut: `Bearer`
- `jwt.access_token.expiration_ms`: Durée de validité du token d'acces en millisecondes. defaut: 1Heure
- `jwt.refresh_token.expiration_ms`: Durée de validité du token de rafraichissement en millisecondes. defaut: 30Jours

## Compilation
Pour le backend
```bash
cd Projet_libre/SportEvents
# Modifier les variables si besoin
# nano src/main/resources/application.properties
chmod +x mvnw && ./mvnw clean package -DskipTests
```
Le binaire est disponible à ce chemin `Projet_libre/SportEvents/target/SportEvents-0.0.1-SNAPSHOT.jar`

Pour le frontend
```bash
# Installer node 18.16.0 si vous utilisez volta
# volta install node@18.16.0
# Installer yarn si nous n'utilisez pas volta
# npm install -g yarn@1.22.19
# Installer les dependances
cd Projet_libre/sport-events-front
yarn install
# Mettre en place les variables d'environnement
cp example.env .env.local
# Modifier les variables d'environnement si besoin
# nano .env.local
# Compiler le projet
yarn build
```

## Lancement en production
```bash
# Lancer le backend
cd Projet_libre/SportEvents && java -jar target/SportEvents-0.0.1-SNAPSHOT.jar
```
L'API est disponible sur le port configuré dans le fichier `application.properties` par défaut `8090`.
```bash
# Lancer le frontend
cd Projet_libre/sport-events-front && yarn start
```
L'app est disponible sur le port configuré dans le fichier `.env.local` par défaut `3000`.
Il est aussi possible d'obtenir le port dans la console apres le lancement du frontend.

## Lancement en développement
```bash
# Lancer le backend
# Modifier les variables si besoin
# nano src/main/resources/application.properties
cd Projet_libre/SportEvents && ./mvnw spring-boot:run
```
L'API est disponible sur le port configuré dans le fichier `application.properties` par défaut `8090`.
La documentation de l'API est disponible au chemin `/swagger-ui.html`.
```bash
# Lancer le frontend
# Mettre en place les variables d'environnement
cp example.env .env.local
# Modifier les variables d'environnement si besoin
# nano .env.local
cd Projet_libre/sport-events-front && yarn dev
```
L'app est disponible sur le port configuré dans le fichier `.env.local` par défaut `3000`.
Il est aussi possible d'obtenir le port dans la console apres le lancement du frontend.

