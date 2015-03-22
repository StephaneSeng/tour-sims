# Apache HTTP Server #
  * Page de téléchargement : http://httpd.apache.org/download.cgi#apache22
  * Remarques :
    * Pas de version de 2.4.1 disponible pour Windows (peut potentiellement provoquer des instabilités apparemment...) : prendre la 2.2.22
    * Utiliser l'installateur pour ne pas devoir configurer à la main
    * L'emplacement de la racine des fichiers servis par Apache est configurable en ajoutant les lignes suivantes au fichier httpd.conf :

```
DocumentRoot "%LOCAL_GIT_REPOSITORY%/ServerRoot"
<Directory "%LOCAL_GIT_REPOSITORY%/ServerRoot">
    Order Allow,Deny
    Allow from All
</Directory>
```

# PHP #
  * Page de téléchargement Linux : http://www.php.net/downloads.php
  * Page de téléchargement Windows : http://windows.php.net/download
  * Remarques :
    * Prendre la version _Thread Safe_ pour pouvoir l'utiliser avec Apache
    * Utiliser l'installateur pour ne pas devoir configurer à la main
    * Lors de l'utilisation de l'installateur, inclure l'installation du module de communication avec PostgreSQL


---


Vous disposez maintenant d'un serveur Apache comprenant le PHP.
Pour tester, ajouter et accéder à la page suivante sur votre serveur :

```
<?php
phpinfo();
?>
```


---


# PostgreSQL #
  * Page de téléchargement : http://www.postgresql.org/download/
  * Remarques :
    * Lors de l'utilisation de l'installateur, installer également le module PostGIS (extension PostgreSQL qui ajoute des fonctionnalités GIS)
    * Normalement, PHP intègre une DLL pour la communication avec PostgreSQL (php\_pgsql.dll). Celle-ci est normalement déjà chargée