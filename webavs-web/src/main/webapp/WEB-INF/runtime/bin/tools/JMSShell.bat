@ECHO JMSShell - Utilitaire proposant un petit shell de test JMS.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - jndiFactory       nom de la classe impl�mentant la factory JNDI
@ECHO - jndiUrlProvider   url du provider JNDI
@ECHO - jmsFactory        nom de la factory JMS
@ECHO - jmsQueue          nom de la queue JMS
@ECHO - [mqTcpHostName]   nom de l'h�te TCP du serveur MQ (optionnel)
@ECHO
@echo off
java -Djava.ext.dirs=..\..\lib lobaz.jade.client.tools.JadeToolJMSShell %1 %2 %3 %4 %5
@echo on

