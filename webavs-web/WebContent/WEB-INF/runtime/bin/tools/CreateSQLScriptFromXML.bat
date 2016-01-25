@ECHO CreateSQLScriptFromXML - Utilitaire permettant de générer le script de création d'une liste de tables à partir de la définition XML des tables.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - xmlFilename     nom du fichier XML contenant les définitions de tables
@ECHO - destFilename    nom du fichier de sortie
@ECHO - [schema]        nom du schéma à utiliser pour préfixer les noms de tables (optionnel, aucun par défaut)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolJdbcCreateMetadataSQL %1 %2 %3
@echo on

