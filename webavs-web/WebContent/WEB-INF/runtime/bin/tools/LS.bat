@ECHO LS - Utilitaire permettant d'afficher le contenu d'un r�pertoire.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - options  options (pas obligatoire), param�tres accept�s: [-l24]
@ECHO - path     nom du r�pertoire
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolLS %1 %2
@echo on