# CheckGlobazStandardsOnDB2 - Utilitaire permettant de contrôler le respect des standards GLOBAZ  dans une base de données DB2.
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur à utiliser pour se connecter à DB2
# - password          mot de passe de l'utilisateur
# - schema            nom du schéma à contrôler
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcCheckStandardDBMetadata COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1 $2 $3 $4

