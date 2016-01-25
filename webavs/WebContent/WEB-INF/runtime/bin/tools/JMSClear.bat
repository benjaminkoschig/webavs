@ECHO JMSClear - Utilitaire permettant d'enlever tous les messages d'une queue JMS.
@ECHO 
@ECHO Les arguments sont:
@ECHO 
@ECHO - jndiFactory       nom de la classe implémentant la factory JNDI
@ECHO - jndiUrlProvider   url du provider JNDI
@ECHO - jmsFactory        nom de la factory JMS
@ECHO - jmsQueue          nom de la queue JMS
@ECHO - [mqTcpHostName]   nom de l'hôte TCP du serveur MQ (optionnel)
@ECHO
@echo off
java -Djava.ext.dirs=..\..\lib globaz.jade.client.tools.JadeToolJMSClear %1 %2 %3 %4 %5
@echo on

