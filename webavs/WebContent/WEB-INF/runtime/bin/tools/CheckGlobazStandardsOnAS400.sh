# CheckGlobazStandardsOnAS400 - Utilitaire permettant de contr�ler le respect des standards GLOBAZ  dans une base de donn�es AS/400.
# 
# Les arguments sont:
# 
# - as400             nom de l'AS/400
# - user              utilisateur � utiliser pour se connecter � l'AS/400
# - password          mot de passe de l'utilisateur
# - schema            nom du sch�ma (biblioth�que AS/400) � contr�ler
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJdbcCheckStandardDBMetadata com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4

