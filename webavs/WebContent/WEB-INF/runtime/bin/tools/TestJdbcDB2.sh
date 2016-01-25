# TestJdbcDB2 - Utilitaire permettant de tester une connexion JDBC vers DB2
# 
# Les arguments sont:
# 
# - db2instance  nom de l'instance DB2
# - user         nom de l'utilisateur pour se connecter à DB2
# - password     mot de passe
# - [schema]     schéma contenant FWSUSRP pour controller la lecture (optionnel)
# 
java -Djava.ext.dirs=../../lib globaz.jdbc.test.TestJdbcDriver COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:%1 %2 %3 %4


