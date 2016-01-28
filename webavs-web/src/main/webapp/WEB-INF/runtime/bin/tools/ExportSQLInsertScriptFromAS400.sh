# ExportSQLInsertScriptFromAS400 - Utilitaire permettant d'exporter le contenu de tables depuis un AS/400 sous forme de scripts SQL INSERT.
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur à utiliser pour se connecter à l'AS/400
# - password          mot de passe de l'utilisateur
# - sourceSchema      nom du schéma (bibliothèque AS/400) source
# - destSchema        nom du schéma destination
# - tableName         nom de la table à traiter ou FILE pour utiliser une liste de tables dans un fichier
# - [tablesFileName]  nom du fichier contenant les tables à traiter (un nom de table par ligne)
# 
java -Djava.ext.dirs=../../lib globaz.globall.tools.BTCreateDB2InsertScript com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1;user=$2;password=$3 $4 $5 $6 $7

