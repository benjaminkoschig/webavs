@ECHO ZIP - Utilitaire permettant de compresser un fichier
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - inputName le nom du fichier à compresser
@ECHO - zipName   le nom du fichier ZIP (optionnel)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.zip.JadeZip %1 %2
@echo on