# CheckGlobazStandardsOnDB2 - Utilitaire permettant de contr�ler le respect des standards GLOBAZ  dans une base de donn�es DB2.
# 
# Les arguments sont:
# 
# - db2instance       nom de l'instance DB2
# - user              utilisateur � utiliser pour se connecter � DB2
# - password          mot de passe de l'utilisateur
# - schema            nom du sch�ma � contr�ler
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcCheckStandardDBMetadata COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:$1 $2 $3 $4

