@ECHO CreateSQLScriptFromXML - Utilitaire permettant de g�n�rer le script de cr�ation d'une liste de tables � partir de la d�finition XML des tables.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - xmlFilename     nom du fichier XML contenant les d�finitions de tables
@ECHO - destFilename    nom du fichier de sortie
@ECHO - [schema]        nom du sch�ma � utiliser pour pr�fixer les noms de tables (optionnel, aucun par d�faut)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolJdbcCreateMetadataSQL %1 %2 %3
@echo on

