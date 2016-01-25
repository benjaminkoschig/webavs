@echo off
cd ..\..

set CLASSPATH=%CLASSPATH%;.\WEB-INF\classes\
rem Add all jars....
for %%i in (".\WEB-INF\lib\*.jar") do call ".\hermesRoot\batch\cpappend.bat" %%i
java globaz.hermes.process.HEExtraitAnnonceProcess %1 %2 %3