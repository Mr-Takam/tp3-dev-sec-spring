# tp3-dev-sec-spring
TP3 - Développement d'applications sécurisées avec Spring Security - Efrei Paris



## Notes de développement (GitHub Codespaces)

### Problème de redirection du port :8080
Dans l'environnement Codespaces, les ports sont mappés sur des sous-domaines (ex: `-8080.app.github.dev`). 
Par défaut, **Spring Security** peut tenter de rediriger vers `localhost:8080/login` ou inclure le port `:8080` à la fin de l'URL publique, ce qui provoque une erreur 404.

**Solution temporaire :**
Si vous êtes redirigé vers une URL contenant `:8080`, supprimez manuellement le `:8080` pour ne garder que le nom de domaine, ou accédez directement à `/login`.

**Solution définitive :**
Nous avons configuré l'application pour qu'elle reconnaisse les headers du proxy de Codespaces (voir `application.properties`).