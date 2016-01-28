@ECHO MySQLCreateImportCommand - Utilitaire permettant de cr�er la ligne de commande pour l'importation de donn�es dans des tables MySQL.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - dbName          nom de la base de donn�es
@ECHO - user            nom de l'utilisateur de MySQL
@ECHO - password        mot de passe de l'utilisateur de MySQL
@ECHO - path            chemin du r�pertoire contenant les fichiers � importer
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.jade.client.tools.JadeToolMySQLCreateImportCommand %1 %2 %3 %4
@echo on
pause
