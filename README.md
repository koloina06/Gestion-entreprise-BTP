# Application de Gestion BTP

Ce projet est une application web de gestion complète pour une entreprise de construction BTP, permettant de gérer les aspects techniques, financiers et administratifs des projets.

## Spécifications Techniques

* **Langage** : Java.
* **Framework** : Spring Boot MVC (Thymeleaf).
* **Base de données** : PostgreSQL.
  
## Fonctionnalités

### Profil Client

* **Connexion par numéro de téléphone** : Les clients se connectent facilement avec leur numéro de téléphone (sans inscription).
* **Liste des devis** : Visualisation de tous les devis associés au client, avec possibilité d'exporter chaque devis en PDF.
* **Création de devis** :
    * Choix du type de maison.
    * Sélection du type de finition (Standard, Gold, Premium, VIP) avec ajustement automatique du prix total.
    * Définition de la date de début des travaux, avec calcul automatique de la date de fin.
* **Paiements** :
    * Enregistrement de paiements multiples avec date et montant.
    * Validation AJAX pour empêcher les paiements dépassant le montant total du devis.
* **Suivi de l'avancement** : Consultation de l'état et de l'avancement des travaux.

### Profil BTP (Administrateur)

* **Connexion sécurisée** : Accès avec identifiant et mot de passe.
* **Tableau de bord de suivi des travaux** :
    * Liste des devis en cours avec montants totaux et paiements effectués.
    * Détail des travaux par devis (type, quantité, prix unitaire, etc.).
    * Colonne "% paiement effectué" dans la liste des devis.
    * Montant total des devis et des paiements effectués.
    * Histogramme des montants des devis par mois et année (avec sélection de l'année).
    * Liste des types de travaux et de finitions avec liens de modification.
* **Gestion des données** :
    * Importation de données pour les maisons, travaux et devis via des fichiers CSV (format fourni).
    * Importation de données pour les paiements via des fichiers CSV (format fourni).
* **Réinitialisation de la base de données** : Fonctionnalité pour réinitialiser la base de données (sauf les identifiants administrateur).

## Auteurs

* [Koloina RALIJAONA - 2024]
