# ExportCSVFileFromAS400 - Utilitaire permettant d'exporter le contenu de tables depuis un AS/400 dans des fichiers CSV
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur à utiliser pour se connecter à l'AS/400
# - password          mot de passe de l'utilisateur
# - schema            nom du schéma (bibliothèque AS/400) à accéder
# - tableName         nom de la table à traiter ou FILE pour utiliser une liste de tables dans un fichier
# - [tablesFileName]  nom du fichier contenant les tables à traiter (un nom de table par ligne)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExport com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4 $5 $6

