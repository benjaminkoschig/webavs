@ECHO JadeServer - Run the JADE server
@echo off
set LOCALCLASSPATH=.

for %%i in ("..\..\lib\*.*") do call "..\lcp.bat" %%i

start java -Djava.awt.headless=true -Xms128m -Xmx1024m -classpath "%LOCALCLASSPATH%" globaz.jade.common.Jade
@echo on

