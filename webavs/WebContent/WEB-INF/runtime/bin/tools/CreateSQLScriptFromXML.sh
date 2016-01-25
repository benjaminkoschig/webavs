# CreateSQLScriptFromXML - Utilitaire permettant de g�n�rer le script de cr�ation d'une liste de tables � partir de la d�finition XML des tables.
# 
# Les arguments sont:
# 
# - xmlFilename     nom du fichier XML contenant les d�finitions de tables
# - destFilename    nom du fichier de sortie
# - [schema]        nom du sch�ma � utiliser pour pr�fixer les noms de tables (optionnel, aucun par d�faut)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcCreateMetadataSQL $1 $2 $3

