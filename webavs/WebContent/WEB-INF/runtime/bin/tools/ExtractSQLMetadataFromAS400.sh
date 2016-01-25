# ExtractSQLMetadataFromAS400 - Utilitaire permettant d'extraire le m�ta-mod�le d'une base de donn�es AS/400 via JDBC vers un fichier texte contenant des instruction SQL.
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur � utiliser pour se connecter � l'AS/400
# - password          mot de passe de l'utilisateur
# - schema            nom du sch�ma (biblioth�que AS/400) � acc�der
# - destFilename      nom du fichier de sortie
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcExtractMetadataSQL com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4 $5

