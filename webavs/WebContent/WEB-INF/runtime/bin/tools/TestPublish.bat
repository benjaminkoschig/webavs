@ECHO TestPublish - Utilitaire permettant tester la publication d'un document
@ECHO 
@echo off
java -Djava.awt.headless=true -Djava.ext.dirs=..\..\lib globaz.jade.publish.test.TestPackageJadePublish
@echo on

