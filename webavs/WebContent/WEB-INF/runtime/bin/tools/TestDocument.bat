@ECHO TestDocument - Utilitaire permettant tester la cr�ation d'un document avec iText.
@ECHO 
@echo off
java -Djava.awt.headless=true -Djava.ext.dirs=..\..\lib globaz.framework.printing.test.TestDocument
@echo on

