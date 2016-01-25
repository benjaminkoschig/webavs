# ExportCSVFileFromDB2 - Utilitaire permettant d'exporter le contenu de tables depuis DB2 dans des fichiers CSV
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur à utiliser pour se connecter à DB2
# - password          mot de passe de l'utilisateur
# - schema            nom du schéma à accéder
# - tableName         nom de la table à traiter ou FILE pour utiliser une liste de tables dans un fichier
# - [tablesFileName]  nom du fichier contenant les tables à traiter (un nom de table par ligne)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExport COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1 $2 $3 $4 $5 $6

