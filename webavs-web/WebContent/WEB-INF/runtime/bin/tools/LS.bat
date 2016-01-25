@ECHO LS - Utilitaire permettant d'afficher le contenu d'un répertoire.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - options  options (pas obligatoire), paramètres acceptés: [-l24]
@ECHO - path     nom du répertoire
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolLS %1 %2
@echo on