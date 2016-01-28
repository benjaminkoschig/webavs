@ECHO ExportCSVFileFromDB2 - Utilitaire permettant d'exporter le contenu de tables depuis DB2 dans des fichiers CSV
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance       nom de l'instance DB2
@ECHO - user              utilisateur � utiliser pour se connecter � DB2
@ECHO - password          mot de passe de l'utilisateur
@ECHO - schema            nom du sch�ma � acc�der
@ECHO - tableName         nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
@ECHO - [tablesFileName]  nom du fichier contenant les tables � traiter (un nom de table par ligne)
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.jade.client.tools.JadeToolJdbcExport COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:%1 %2 %3 %4 %5 %6
@echo on
pause
