@ECHO DB2CreateImportScript - Utilitaire permettant de créer un script d'importation de données dans des tables DB2.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance     nom de l'instance DB2
@ECHO - schema          nom du schéma à accéder
@ECHO - path            chemin du répertoire contenant les fichiers à importer
@ECHO - [commitCount]   le nombre d'insertions avant un commit (optionnel, défaut=1000)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolDB2CreateImportScript %1 %2 %3 %4
@echo on
pause