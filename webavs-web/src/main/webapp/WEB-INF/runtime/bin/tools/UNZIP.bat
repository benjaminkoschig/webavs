@ECHO UNZIP - Utilitaire permettant de décompresser un fichier
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - zipName   le nom du fichier ZIP
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.zip.JadeUnzip %1
@echo on