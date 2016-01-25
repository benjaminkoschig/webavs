# CreateSQLScriptFromXML - Utilitaire permettant de générer le script de création d'une liste de tables à partir de la définition XML des tables.
# 
# Les arguments sont:
# 
# - xmlFilename     nom du fichier XML contenant les définitions de tables
# - destFilename    nom du fichier de sortie
# - [schema]        nom du schéma à utiliser pour préfixer les noms de tables (optionnel, aucun par défaut)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcCreateMetadataSQL $1 $2 $3

