# Remarques préliminaires #

Il avait été prévu de fournir à l'utilisateur un mode hors-ligne permettant l'utilisation de Tour'SIMS sans avoir besoin d'une connexion.

Malheureusement, par manque de temps/de ressources, nous n'avons pas pu finaliser et intégrer ce mode hors-ligne dans la release 03.

Voici cependant la procédure que nous avons suivi afin d'intégrer une base de données de points d'intérêts (POIs) d'OpenStreetMap dans notre propre base de données.

# Téléchargement d'un dump #

  * Télécharger sur une machine un dump de la base de données d'OpenStreetMap disponibles ici par exemple : http://download.geofabrik.de/osm/europe/france/
    * Privilégier le format de fichier PBF pusique celui-ci est compressé et est utilisable directement par les outils que nous allons déployer par la suite ;

Plus d'informations ici : http://help.openstreetmap.org/questions/15/getting-point-of-interest-data-from-openstreetmap

# Création du schéma de la base de donnée locale (PostgreSQL 9.1) #

Voici les commandes à exécuter depuis la ligne de commande Windows, à adapter au besoin (les programmes createdb, createlang... sont dans le répertoire d'installation de PostgreSQL) :

```
runas /user:postgres cmd
createdb osm
createlang plpgsql osm
psql -d osm -f "C:\Program Files\PostgreSQL\9.1\share\extension\hstore--1.0.sql"
psql -d osm -f "C:\Program Files\PostgreSQL\9.1\share\contrib\postgis-1.5\postgis.sql"
psql -d osm -f "C:\Program Files\PostgreSQL\9.1\share\contrib\postgis-1.5\spatial_ref_sys.sql"

psql -d osm -f "C:\Program Files\Osmosis\script\pgsnapshot_schema_0.6.sql"
(à faire plusieurs fois en cas d'erreur pendant les exécutions)
```

Plus d'informations ici (dont les tests pour s'assurer de la validité du schéma qui a été créé) : http://wiki.openstreetmap.org/wiki/Osmosis/PostGIS_Setup

# Insertion des données du (des) dump(s) dans la base de données locale #

Cette étape va nécessiter l'utilisation de l'utilitaire Osmosis, dont l'utilisation est décrite ici : http://wiki.openstreetmap.org/wiki/Osmosis

Deux exemples de cas d'utilisation sont donnés ci-dessous, encore une fois à adapter au besoin. Ces exemples diffèrent dans le nombre de fichiers PBF à insérer dans la base de données. En effet, à cause de la gestion des contraintes SQL par Osmosis, il n'est possible d'effectuer l'opération d'insertion des données PBF vers la base de données qu'une seule fois. S'il y a plusieurs PBFs en entrée, il faut au préalable les fusionner puis utiliser le PBF résultant pour l'opération d'insertion.

  * Cas où il n'y a qu'un seul PBF à insérer :
```
"C:\Program Files\Osmosis\bin\osmosis.bat" --read-pbf file="D:\Téléchargements\rhone-alpes.osm.pbf" --node-key keyList="geological,historic,leisure,public_transport,shop,sport,tourism" --write-pgsql host="localhost" database="osm" user="postgres" password=""
```

  * Cas où plusieurs PBF sont à insérer :
```
"C:\Program Files\Osmosis\bin\osmosis.bat" --read-pbf file="D:\Téléchargements\ile-de-france.osm.pbf" --node-key keyList="geological,historic,leisure,public_transport,shop,sport,tourism" --sort --write-pbf file="D:\Téléchargements\ile-de-france-filtered.osm.pbf"
"C:\Program Files\Osmosis\bin\osmosis.bat" --read-pbf file="D:\Téléchargements\rhone-alpes.osm.pbf" --node-key keyList="geological,historic,leisure,public_transport,shop,sport,tourism" --sort --write-pbf file="D:\Téléchargements\rhone-alpes-filtered.osm.pbf"
"C:\Program Files\Osmosis\bin\osmosis.bat" --read-pbf file="D:\Téléchargements\ile-de-france-filtered.osm.pbf" --read-pbf file="D:\Téléchargements\rhone-alpes-filtered.osm.pbf" --merge --write-pbf file="D:\Téléchargements\merged.osm.pbf"
"C:\Program Files\Osmosis\bin\osmosis.bat" --read-pbf file="D:\Téléchargements\merged.osm.pbf" --write-pgsql host="localhost" database="osm" user="postgres" password=""
```

Les exemples précédent ont également réalisés une opération de filtrage des POIs avant de réaliser l'insertion de ces points dans la base de données. Pour plus d'informations sur les catégories des POIs, on pourra se reporter au lien suivant : http://wiki.openstreetmap.org/wiki/Map_Features

# Intégration à l'application Android #

Outre l'intégration des POIs, il avait été prévu une intégration des cartes/tiles d'OpenStreetMap dans l'application pour ainsi de défaire totalement de Google Maps. Pour cela, nous avions envisagé d'intégrer la librairie mapsforge (http://code.google.com/p/mapsforge/) qui génère les tiles à partir d'un fichier MAP présent dans l'appareil Android.

Il est à noter que Osmosis permet de générer des fichiers MAPs en utilisant une commande semblable à celle-ci :

```
"C:\Program Files\Osmosis\bin\osmosis.bat" --read-pbf file="D:\Téléchargements\rhone-alpes.osm.pbf" --mapfile-writer file="D:\Téléchargements\rhone-alpes.map"
```