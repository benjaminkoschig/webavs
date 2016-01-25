@ECHO TestJdbcAS400 - Utilitaire permettant de tester une connexion JDBC vers un AS/400
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - as400        nom de l'AS/400
@ECHO - user         nom de l'utilisateur pour se connecter à l'AS/400
@ECHO - password     mot de passe
@ECHO - [schema]     schéma contenant FWSUSRP pour controller la lecture (optionnel)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jdbc.test.TestJdbcDriver com.ibm.as400.access.AS400JDBCDriver jdbc:as400://%1 %2 %3 %4
@echo on

