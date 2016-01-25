@ECHO GZ - Utilitaire permettant de compresser le contenu d'un fichier
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - inputName le nom du fichier à compresser
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.zip.JadeGZip %1
@echo on