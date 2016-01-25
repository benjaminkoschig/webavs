@ECHO TestJdbcDB2 - Utilitaire permettant de tester une connexion JDBC vers DB2
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance  nom de l'instance DB2
@ECHO - user         nom de l'utilisateur pour se connecter à DB2
@ECHO - password     mot de passe
@ECHO - [schema]     schéma contenant FWSUSRP pour controller la lecture (optionnel)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jdbc.test.TestJdbcDriver COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:%1 %2 %3 %4
@echo on

