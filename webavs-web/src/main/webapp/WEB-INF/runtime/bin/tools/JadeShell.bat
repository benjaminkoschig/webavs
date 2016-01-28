@ECHO JadeShell - Shell to access Globaz JADE system
@echo off
java -Djava.awt.headless=true -Djava.ext.dirs=..\..\lib -Xms128m -Xmx1024m globaz.jade.shell.JadeShell
@echo on

