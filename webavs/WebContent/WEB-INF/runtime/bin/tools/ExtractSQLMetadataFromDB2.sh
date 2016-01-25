# ExtractSQLMetadataFromDB2 - Utilitaire permettant d'extraire le méta-modèle d'une base de données DB2 via JDBC vers un fichier texte contenant des instruction SQL.
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur à utiliser pour se connecter à DB2
# - password          mot de passe de l'utilisateur
# - schema            nom du schéma à accéder
# - destFilename      nom du fichier de sortie
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExtractMetadataSQL COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1 $2 $3 $4 $5

