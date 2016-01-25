INTRODUCTION TECHNIQUE A HERA

1er octobre 2005, version 1.0
Michael Muller

1. STRUCTURE GENERALE
**********************

L'application est compos�e de 4 �crans: 4 x (_rc, _rcListe, _de).
Chaque �cran est g�r�e par une classe Action qui g�re les actions de ces �crans:
l'�cran apercuEnfant_x.jsp  mappe sur l'action SFApercuEnfantAction
l'�cran apercuRelationConjoint_x.jsp mappe sur l'action SFApercuRelationConjointAction
l'�cran apercuRelationFamiliale_x.jsp mappe sur l'action SFApercuRelationFamilialeAction
l'�cran periode_x.jsp mappe sur l'action SFPeriodeAction

La pluspart de ces �crans repr�sentent plusieurs tables, 
ainsi l'�cran apercuEnfant_x.jsp utilise la classe SFApercuEnfant qui fait une jointure entre SFMembreFamille et SFEnfant
l'�cran apercuRelationConjoint_x.jsp utilise la classe SFApercuRelationConjoint qui fait une jointure entre SFRelationConjoint, SFConjoint et SFMembreFamille (2 fois: une jointure sur SFMembreFamille par conjoint)
l'�cran apercuRelationFamiliale_x.jsp utilise la classe SFApercuRequerant qui fait une jointure entre SFMembreFamille et SFRequerant, et la classe SFApercuRelationFamilialeRequerant qui fait une jointure sur RelationFamilialeRequerent et SFMembreFamille
l'�cran periode_x.jsp utilise la classe SFPeriode

la classe SFMembreFamille contient les membre de famille, ceux-ci sont enregistr�s dans les tiers (table TITIERP, TIPAVSP, TIPERSP)
lorsqu'un num�ro AVS est renseign�, la table SFMBRFAM ne contien que l'id du tiers. 
Si le n� AVS n'est pas renseign�, tous les infos du membre sont enregistr� "en local" (dans la table SFMBRFAM)

Note: les classes qui ne commencent pas par SFApercu... sont des classes qui mappent directement sur une table.




2. INTEGRATION DE HERA DANS D'AUTRES APPLICATIONS
**************************************************
Appeler l'action en passant en param�tre:
 - l'url de retour pr�c�damment encod�e en utilisant la m�thode: globaz.hera.external.ISFUrlEncode.encodeUrl("mon URL de retour")
 - idTiers du tiers dont on souhaite construire la situation familiale. Le tiers d�vient requ�rant et est mis en session pour la constuction de la famille
 
Et appeler l'action...
/hera?userAction=hera.famille.apercuRelationFamilialeRequerant.entrerApplication&idTiers=xxxxx&urlFrom=yyyyyyy


3. HERA INTERFACE
******************

L'impl�mentation des interface se fait dans le package globaz.hera.impl .
Par d�faut, l'impl�mentation du domaine d'application standard est appel�e.
Pour faire varier le comportement d'une m�thode en fonction d'un domaine d'application, surcharger la classe SFSituationFamilliale.

La classe m�re pour extraire des informations depuis la Situation Familiale est globaz.hera.api.ISFSituationFamiliale.
En effet, tous les objets (BEntity) sont retourn�s par les getter des m�thodes de cette classes.
Aucune famille ne peut �tre cr�e ou modifi�e depuis l'interface.

UTILISATION DE L'INTERFACE:

BIApplication application = GlobazSystem.getApplication("HERA_REMOTE");
BSession session = (BSession) application.newSession("globazf", "ssiiadm");
ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_STANDARD);

//A partir d'une instance de ISFSituationFamiliale en fonction du domaine, 
//toutes les m�thodes concerant la situation familale peuvent �tre appel�es
ISFMembreFamilleRequerant[] result = sf.getMembresFamille("257667","12.12.1998");
