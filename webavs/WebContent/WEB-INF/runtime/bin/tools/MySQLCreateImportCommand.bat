@ECHO MySQLCreateImportCommand - Utilitaire permettant de créer la ligne de commande pour l'importation de données dans des tables MySQL.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - dbName          nom de la base de données
@ECHO - user            nom de l'utilisateur de MySQL
@ECHO - password        mot de passe de l'utilisateur de MySQL
@ECHO - path            chemin du répertoire contenant les fichiers à importer
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.jade.client.tools.JadeToolMySQLCreateImportCommand %1 %2 %3 %4
@echo on
pause
