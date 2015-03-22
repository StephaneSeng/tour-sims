# Sommaire #



# Description des fichiers présents sur le repository #

  * Les scripts PHP utilisés par l'application Android pour requêter sur la base de données doivent être placés à la racine du serveur. Ces fichiers sont dans le répertoire ServerRoot du repository.

  * Les scripts d'initialisation, d'alimentation et de nettoyage de la base de données de l'application sont dans le répertoire Scripts. Des scripts Windows Shell (Initialize|Populate|Truncate)Database.bat sont disponibles pour faciliter la mise en place de la base de données dans un environnement de test. Le script GenerateOnlineScripts.bat génère 3 scripts SQL destinés à être exécutés sur une interface Web liée à la base de données de déploiement.

  * Le répertoire InterfaceWeb contient également des fichiers à déposer à la racine du serveur Web. On peut y trouver l'interface Web de construction de parcours. Les parcours générés sont déposés sur le serveur sous la forme de fichiers KML dans le sous-répertoire kml du serveur.

  * Le projet ActionBar est une copie du projet ActionBarSherlock de Jake Wharton. Nous avons fait le choix d'utiliser cette librairie pour nous faciliter l'implémentation du desing pattern de l'Action Bar présent dans Android 3+, y compris dans les versions antérieures.

  * Le projet Mobile est un projet Eclipse (Android ADT). Il s'agit du projet principal de notre application.

# Instructions de déploiement #

  * Sur le serveur :
    * Installer le serveur Web + PHP ;
    * Installer le système de gestion de base de données PostgreSQL ;
    * Exécuter les scripts SQL générés pour mettre en place la base de données toursims ;
    * Téléverser les scripts PHP à la racine du serveur ;
    * Faire de même pour les fichiers de l'interface Web de création de parcours.

  * Sur l'environnement de développement Android :
    * Installer l'environnement de développement Android (Android SDK) ;
      * Sélectionner au moins l'API 15 pour pouvoir utiliser ActionBarSherlock (prendre Google APIs pour Maps) ;
    * Créer un nouveau Workspace Eclipse ;
    * Importer le projet librairie ActionBar (créer un nouveau projet puis utiliser des fichiers existants) ;
    * Importer le projet Mobile :
      * Pour que ce projet compile, il faut que le projet ActionBar soit également ouvert dans le Workspace et que Mobile puisse l'utiliser (propriétés du projet Mobile > Add Library) ;
      * Les deux projets doivent utiliser l'API 15 en tant que target.

# Common pitfalls #

  * Java 1.6 pour utiliser ActionBar ;
  * Faire des Clean quand on a des problèmes, si R n'est pas généré alors une erreur existe dans des fichiers autre que les .java : AndroidManifest, res/`*` (layout, values...).

# Compatibilité #

  * Testé sous Android 2.3.x ;
  * Incompatible avec Android 4 (malheureusement, erreur de conception avec la façon de gérer les requêtes réseau, demande trop d'effort maintenant pour corriger ça pusiqu'on s'en est rendu compte qu'à la toute fin du projet).

# Rétroingénierie #

  * Schéma de la base de données Tour'SIMS (ouvrir dans un nouvel onglet pour mieux voir) : ![http://tour-sims.googlecode.com/git/Resources/DatabaseSchema.png](http://tour-sims.googlecode.com/git/Resources/DatabaseSchema.png)

# Liens vers les projets tiers utilisés #

  * ActionBarSherlock : http://actionbarsherlock.com et https://github.com/JakeWharton/ActionBarSherlock
  * ActionBarSherlock (maps) : https://github.com/JakeWharton/ActionBarSherlock-Plugin-Maps
  * Android Icons : http://www.androidicons.com/

Schémas réalisés à l'aide de ObjectAid (http://www.objectaid.com/) et de dbVisualizer (http://www.dbvis.com/).