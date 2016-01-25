@ECHO ExtractXMLMetadataFromDB2 - Utilitaire permettant d'extraire le m�ta-mod�le d'une base de donn�es DB2 via JDBC vers un fichier XML
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - db2instance       nom de l'instance DB2
@ECHO - user              utilisateur � utiliser pour se connecter � DB2
@ECHO - password          mot de passe de l'utilisateur
@ECHO - schema            nom du sch�ma � acc�der
@ECHO - destFilename      nom du fichier de sortie
@ECHO 
@echo off
java -Djava.ext.dirs=..\..\lib -Xms1024m -Xmx1024m globaz.jade.client.tools.JadeToolJdbcExtractMetadataXML COM.ibm.db2.jdbc.app.DB2Driver jdbc:db2:%1 %2 %3 %4 %5 
@echo on
pause
