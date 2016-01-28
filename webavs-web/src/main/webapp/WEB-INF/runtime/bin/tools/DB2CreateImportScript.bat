@ECHO DB2CreateImportScript - Utilitaire permettant de cr�er un script d'importation de donn�es dans des tables DB2.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance     nom de l'instance DB2
@ECHO - schema          nom du sch�ma � acc�der
@ECHO - path            chemin du r�pertoire contenant les fichiers � importer
@ECHO - [commitCount]   le nombre d'insertions avant un commit (optionnel, d�faut=1000)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolDB2CreateImportScript %1 %2 %3 %4
@echo on
pause