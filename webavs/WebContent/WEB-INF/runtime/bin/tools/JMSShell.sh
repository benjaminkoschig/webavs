# JMSShell - Shell permettant d'interroger une queue de messages JMS.
# 
# Les arguments sont:
# 
# - jndiFactory       nom de la classe implémentant la factory JNDI
# - jndiUrlProvider   url du provider JNDI
# - jmsFactory        nom de la factory JMS
# - jmsQueue          nom de la queue JMS
# - [mqTcpHostName]   nom de l'hôte TCP du serveur MQ (optionnel)
#
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolJMSShell $1 $2 $3 $4 $5

