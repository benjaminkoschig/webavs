@ECHO CheckGlobazStandardsOnDB2 - Utilitaire permettant de contr�ler le respect des standards GLOBAZ  dans une base de donn�es DB2.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance       nom de l'instance DB2
@ECHO - user              utilisateur � utiliser pour se connecter � DB2
@ECHO - password          mot de passe de l'utilisateur
@ECHO - schema            nom du sch�ma � contr�ler
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.jade.client.tools.JadeToolJdbcCheckStandardDBMetadata COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:%1 %2 %3 %4
@echo on
pause
