@ECHO GZU - Utilitaire permettant de d�compresser le contenu d'un fichier
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - gzipName le nom du fichier ayant le contenu � d�compresser
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.zip.JadeUnGZip %1
@echo on