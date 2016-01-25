# CheckGlobazStandardsOnAS400 - Utilitaire permettant de contrôler le respect des standards GLOBAZ  dans une base de données AS/400.
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur à utiliser pour se connecter à l'AS/400
# - password          mot de passe de l'utilisateur
# - schema            nom du schéma (bibliothèque AS/400) à contrôler
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcCheckStandardDBMetadata com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4

