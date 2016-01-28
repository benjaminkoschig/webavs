@ECHO ExtractSQLMetadataFromAS400 - Utilitaire permettant d'extraire le méta-modèle d'une base de données AS/400 via JDBC vers un fichier texte contenant des instruction SQL.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - as400             nom de l'AS/400
@ECHO - user              utilisateur à utiliser pour se connecter à l'AS/400
@ECHO - password          mot de passe de l'utilisateur
@ECHO - schema            nom du schéma (bibliothèque AS/400) à accéder
@ECHO - destFilename      nom du fichier de sortie
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.jade.client.tools.JadeToolJdbcExtractMetadataSQL com.ibm.as400.access.AS400JDBCDriver jdbc:as400://%1 %2 %3 %4 %5
@echo on
pause
