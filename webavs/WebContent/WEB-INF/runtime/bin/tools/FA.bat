@ECHO FA - Utilitaire permettant d'obtenir les attributs d'un fichier.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - filename          nom du fichier
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolFA %1
@echo on
