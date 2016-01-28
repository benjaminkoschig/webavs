# ExportCSVFileFromAS400 - Utilitaire permettant d'exporter le contenu de tables depuis un AS/400 dans des fichiers CSV
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur � utiliser pour se connecter � l'AS/400
# - password          mot de passe de l'utilisateur
# - schema            nom du sch�ma (biblioth�que AS/400) � acc�der
# - tableName         nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
# - [tablesFileName]  nom du fichier contenant les tables � traiter (un nom de table par ligne)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExport com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4 $5 $6

