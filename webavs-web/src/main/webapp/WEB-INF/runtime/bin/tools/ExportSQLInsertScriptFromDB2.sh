# ExportSQLInsertScriptFromDB2 - Utilitaire permettant d'exporter le contenu de tables depuis DB2 sous forme de scripts SQL INSERT.
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur � utiliser pour se connecter � DB2
# - password          mot de passe de l'utilisateur
# - sourceSchema      nom du sch�ma source
# - destSchema        nom du sch�ma destination
# - tableName         nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
# - [tablesFileName]  nom du fichier contenant les tables � traiter (un nom de table par ligne)
# 
java -Djava.ext.dirs=../../lib globaz.globall.tools.BTCreateDB2InsertScript COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1;user=$2;password=$3 $4 $5 $6 $7

