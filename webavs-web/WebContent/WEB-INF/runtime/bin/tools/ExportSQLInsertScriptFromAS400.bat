@ECHO ExportSQLInsertScriptFromAS400 - Utilitaire permettant d'exporter le contenu de tables depuis un AS/400 sous forme de scripts SQL INSERT.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - as400             nom de l'AS/400
@ECHO - user              utilisateur à utiliser pour se connecter à l'AS/400
@ECHO - password          mot de passe de l'utilisateur
@ECHO - sourceSchema      nom du schéma (bibliothèque AS/400) source
@ECHO - destSchema        nom du schéma destination
@ECHO - tableName         nom de la table à traiter ou FILE pour utiliser une liste de tables dans un fichier
@ECHO - [tablesFileName]  nom du fichier contenant les tables à traiter (un nom de table par ligne)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.globall.tools.BTCreateDB2InsertScript com.ibm.as400.access.AS400JDBCDriver jdbc:as400://%1;user=%2;password=%3 %4 %5 %6 %7
@echo on
pause
