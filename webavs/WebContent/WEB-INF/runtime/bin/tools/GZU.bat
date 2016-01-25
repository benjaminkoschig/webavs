@ECHO GZU - Utilitaire permettant de décompresser le contenu d'un fichier
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - gzipName le nom du fichier ayant le contenu à décompresser
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.zip.JadeUnGZip %1
@echo on