# ExtractSQLMetadataFromDB2 - Utilitaire permettant d'extraire le m�ta-mod�le d'une base de donn�es DB2 via JDBC vers un fichier texte contenant des instruction SQL.
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur � utiliser pour se connecter � DB2
# - password          mot de passe de l'utilisateur
# - schema            nom du sch�ma � acc�der
# - destFilename      nom du fichier de sortie
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExtractMetadataSQL COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1 $2 $3 $4 $5

