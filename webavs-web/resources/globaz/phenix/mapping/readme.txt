G�n�ration des classes  de mapping xml vers objets Java
A partir d'un sch�ma xml (xsd), on g�n�re des classes de mapping qui transforment des documents xml conforment au sch�ma en objets.

Pour castor:
- supprimer les fichiers g�n�r�s auparavant par CASTOR lors d'une reg�n�ration
- executer les commandes ci-dessous dans un shell depuis la racine de l'espace de travail (workspace p.ex "D:\Websphere\webavs_workspace>")
	avec les jars  et le xsd dans le repertoire courant

R�ception
java -classpath ".\webavs\Web Content\WEB-INF\lib\castor-0.9.6.jar";".\webavs\Web Content\WEB-INF\lib\xercesImpl.jar";".\webavs\Web Content\WEB-INF\lib\commons-logging.jar" org.exolab.castor.builder.SourceGenerator -i .\phenix\source\globaz\phenix\mapping\retour\GlobazCommunicationRetour.xsd -dest .\phenix\source\ -package globaz.phenix.mapping.retour.castor

Demande
java -classpath ".\webavs\Web Content\WEB-INF\lib\castor-0.9.6.jar";".\webavs\Web Content\WEB-INF\lib\xercesImpl.jar";".\webavs\Web Content\WEB-INF\lib\commons-logging.jar" org.exolab.castor.builder.SourceGenerator -i .\phenix\source\globaz\phenix\mapping\demande\GlobazCommunicationDemande.xsd -dest .\phenix\source\ -package globaz.phenix.mapping.demande.castor

