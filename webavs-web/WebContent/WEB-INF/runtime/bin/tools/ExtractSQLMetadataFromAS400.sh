# ExtractSQLMetadataFromAS400 - Utilitaire permettant d'extraire le méta-modèle d'une base de données AS/400 via JDBC vers un fichier texte contenant des instruction SQL.
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur à utiliser pour se connecter à l'AS/400
# - password          mot de passe de l'utilisateur
# - schema            nom du schéma (bibliothèque AS/400) à accéder
# - destFilename      nom du fichier de sortie
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExtractMetadataSQL com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4 $5

