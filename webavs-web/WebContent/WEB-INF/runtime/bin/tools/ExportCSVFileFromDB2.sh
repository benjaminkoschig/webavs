# ExportCSVFileFromDB2 - Utilitaire permettant d'exporter le contenu de tables depuis DB2 dans des fichiers CSV
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur � utiliser pour se connecter � DB2
# - password          mot de passe de l'utilisateur
# - schema            nom du sch�ma � acc�der
# - tableName         nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
# - [tablesFileName]  nom du fichier contenant les tables � traiter (un nom de table par ligne)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExport COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1 $2 $3 $4 $5 $6

