@ECHO ExportSQLInsertScriptFromDB2 - Utilitaire permettant d'exporter le contenu de tables depuis DB2 sous forme de scripts SQL INSERT.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance       nom de l'instance DB2
@ECHO - user              utilisateur � utiliser pour se connecter � DB2
@ECHO - password          mot de passe de l'utilisateur
@ECHO - sourceSchema      nom du sch�ma source
@ECHO - destSchema        nom du sch�ma destination
@ECHO - tableName         nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
@ECHO - [tablesFileName]  nom du fichier contenant les tables � traiter (un nom de table par ligne)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.globall.tools.BTCreateDB2InsertScript COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:%1;user=%2;password=%3 %4 %5 %6 %7
@echo on
pause
