# Projet : Banque Simplifiée - Architecture et Sécurité (Spring Boot)

**Module :** Développement sécurisé (DevSec) - INGE-3-APP-LSI 
**Binôme :** Cyril Takam & Abdou Khadre DIOP
**Date de rendu :** 24 février 2026 

<br>

## 1. Contexte et Objectifs

L'objectif de ce projet est de concevoir et sécuriser une application bancaire simplifiée en s'appuyant exclusivement sur l'écosystème Spring (Core, Web, Data et Security). Conformément au principe de *Security By Design*, l'intégralité des fonctions techniques (authentification, autorisations, chiffrement, protection contre les injections, gestion CORS/CSRF, routage HTTP et persistance) est déléguée au framework.

Les comptes utilisateurs de base sont pré-enregistrés manuellement dans la base de données.

<br>

## 2. Architecture et Maintenabilité

Pour garantir une maintenabilité optimale de l'application , nous avons opté pour une architecture en couches (N-Tiers) avec des **contrôleurs mixtes**:

* **Couche Présentation (Front-end) :** Générée par Spring Web et Thymeleaf. Cela permet une intégration native et sécurisée avec Spring Security (gestion automatique des sessions et des jetons CSRF).
* **Couche Web (Controllers) :** Routage HTTP géré par `@Controller` (pour les vues) et `@RestController` (pour d'éventuels endpoints utilitaires).
* **Couche Métier (Services) :** Contient la logique applicative et les règles de sécurité fines (*Method Security*). Séparation stricte via des interfaces.
* **Couche Persistance (Repositories) :** Déléguée à Spring Data JPA / Hibernate pour la communication sécurisée avec la base de données.


<br>

## 3. Modélisation des Acteurs et Contrôle d'Accès

Le système implémente un contrôle d'accès hybride (RBAC pour les rôles, ABAC via SpEL pour les règles métier) :

* **VISITEUR (Non authentifié) :** * Peut uniquement accéder à la page de connexion (`/login`).


* **CLIENT (Rôle `USER`) :** * Peut consulter **exclusivement** ses propres comptes bancaires.
    * Peut réaliser des opérations de crédit et de débit plafonnées à **1000 € maximum**.




* **CONSEILLER BANCAIRE (Rôle `ADMIN`) :** * Peut créer des comptes bancaires pour les clients existants.

    * Peut consulter **tous** les comptes bancaires du système.

    * Peut réaliser des opérations de crédit et de débit, mais **strictement pour des montants supérieurs à 1000 €**.



<br>

## 4. Matrice de Sécurité Appliquée (Standard OWASP & Exigences TP)

L'application délègue intégralement les aspects sécuritaires à Spring Security. Voici comment les menaces sont mitigées :

### A01:2021 - Broken Access Control & IDOR

* **Filtrage des URLs :** Utilisation du `SecurityFilterChain` pour restreindre l'accès aux routes `/admin/**` et `/client/**`.
* **Method Security (`@PreAuthorize` / `@PostAuthorize`) :** Les règles de montants et d'appartenance des comptes sont appliquées directement sur les méthodes des Services.
* *Exemple métier :* `@PreAuthorize("hasRole('ADMIN') and #montant > 1000 or hasRole('CLIENT') and #montant <= 1000")` pour la méthode de transaction.
* *Exemple confidentialité :* `@PostAuthorize("returnObject.proprietaire.login == authentication.name or hasRole('ADMIN')")` pour la consultation de compte.



### A02:2021 - Cryptographic Failures

* **Chiffrement :** Les mots de passe en base de données sont hachés avec `BCryptPasswordEncoder`. Aucun mot de passe n'est manipulé en clair par la couche applicative.



### A03:2021 - Injection (SQL & XSS) 

* **Injections SQL :** Éradiquées grâce à l'utilisation exclusive de Spring Data JPA (`JpaRepository`) qui utilise des requêtes préparées sous le capot.


* **Injections de code (XSS) :** Neutralisées par le moteur de template Thymeleaf qui échappe par défaut toutes les variables rendues dans le HTML (`th:text`).

### A07:2021 - Identification and Authentication Failures

* **Authentification Robuste :** Gestion complète par Spring Security via une implémentation sur-mesure de `UserDetailsService` connectée à notre base de données via Spring Data JPA.



### Falsification de requêtes (CSRF) & Partage de ressources (CORS) 

* **Protection CSRF :** La protection CSRF de Spring Security est **activée par défaut**. Les formulaires Thymeleaf injectent dynamiquement le token `_csrf` pour protéger toutes les requêtes de modification d'état (POST, PUT, DELETE).
* **Configuration CORS :** Bien que l'architecture MVC limite les requêtes Cross-Origin, une politique CORS restrictive est déclarée au niveau du `SecurityFilterChain` pour n'autoriser que l'origine du serveur hébergeant l'application.

<br>

## 5. Livrables du Projet

Conformément aux spécifications, les livrables suivants sont fournis dans notre dépôt Moodle:

1. **Diagramme d'architecture** détaillé de l'application (disponible à la racine du projet).

2. **Code source** complet et remanié de l'application.

3. **Démonstration sur machine** illustrant les protections mises en place (prévue le 24 mars 2026).



