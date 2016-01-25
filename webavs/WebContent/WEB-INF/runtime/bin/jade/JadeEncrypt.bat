@ECHO JadeEncrypt - Globaz Encryption Tool
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - text       texte à encrypter
@echo off
java -cp ../../lib/jade13.jar;../../lib/jce1_2_2.jar;../../lib/local_policy.jar;../../lib/sunjce_provider.jar;../../lib/US_export_policy.jar globaz.jade.crypto.JadeEncrypt %1 %2 %3 %4 %5 %6 %7 %8 %9
@echo on

