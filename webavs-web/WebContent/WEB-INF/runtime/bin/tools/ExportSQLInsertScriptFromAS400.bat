@ECHO ExportSQLInsertScriptFromAS400 - Utilitaire permettant d'exporter le contenu de tables depuis un AS/400 sous forme de scripts SQL INSERT.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - as400             nom de l'AS/400
@ECHO - user              utilisateur � utiliser pour se connecter � l'AS/400
@ECHO - password          mot de passe de l'utilisateur
@ECHO - sourceSchema      nom du sch�ma (biblioth�que AS/400) source
@ECHO - destSchema        nom du sch�ma destination
@ECHO - tableName         nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
@ECHO - [tablesFileName]  nom du fichier contenant les tables � traiter (un nom de table par ligne)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.globall.tools.BTCreateDB2InsertScript com.ibm.as400.access.AS400JDBCDriver jdbc:as400://%1;user=%2;password=%3 %4 %5 %6 %7
@echo on
pause
