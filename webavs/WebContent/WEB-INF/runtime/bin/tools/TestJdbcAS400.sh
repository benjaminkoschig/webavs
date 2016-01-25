# TestJdbcAS400 - Utilitaire permettant de tester une connexion JDBC vers un AS/400
# 
# Les arguments sont:
# 
# - as400        nom de l'AS/400
# - user         nom de l'utilisateur pour se connecter à l'AS/400
# - password     mot de passe
# - [schema]     schéma contenant FWSUSRP pour controller la lecture (optionnel)
# 
java -Djava.ext.dirs=../../lib globaz.jdbc.test.TestJdbcDriver com.ibm.as400.access.AS400JDBCDriver jdbc:as400://$1 $2 $3 $4

