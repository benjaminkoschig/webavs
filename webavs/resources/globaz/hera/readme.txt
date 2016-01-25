INTRODUCTION TECHNIQUE A HERA

1er octobre 2005, version 1.0
Michael Muller

1. STRUCTURE GENERALE
**********************

L'application est composée de 4 écrans: 4 x (_rc, _rcListe, _de).
Chaque écran est gérée par une classe Action qui gère les actions de ces écrans:
l'écran apercuEnfant_x.jsp  mappe sur l'action SFApercuEnfantAction
l'écran apercuRelationConjoint_x.jsp mappe sur l'action SFApercuRelationConjointAction
l'écran apercuRelationFamiliale_x.jsp mappe sur l'action SFApercuRelationFamilialeAction
l'écran periode_x.jsp mappe sur l'action SFPeriodeAction

La pluspart de ces écrans représentent plusieurs tables, 
ainsi l'écran apercuEnfant_x.jsp utilise la classe SFApercuEnfant qui fait une jointure entre SFMembreFamille et SFEnfant
l'écran apercuRelationConjoint_x.jsp utilise la classe SFApercuRelationConjoint qui fait une jointure entre SFRelationConjoint, SFConjoint et SFMembreFamille (2 fois: une jointure sur SFMembreFamille par conjoint)
l'écran apercuRelationFamiliale_x.jsp utilise la classe SFApercuRequerant qui fait une jointure entre SFMembreFamille et SFRequerant, et la classe SFApercuRelationFamilialeRequerant qui fait une jointure sur RelationFamilialeRequerent et SFMembreFamille
l'écran periode_x.jsp utilise la classe SFPeriode

la classe SFMembreFamille contient les membre de famille, ceux-ci sont enregistrés dans les tiers (table TITIERP, TIPAVSP, TIPERSP)
lorsqu'un numéro AVS est renseigné, la table SFMBRFAM ne contien que l'id du tiers. 
Si le n° AVS n'est pas renseigné, tous les infos du membre sont enregistré "en local" (dans la table SFMBRFAM)

Note: les classes qui ne commencent pas par SFApercu... sont des classes qui mappent directement sur une table.




2. INTEGRATION DE HERA DANS D'AUTRES APPLICATIONS
**************************************************
Appeler l'action en passant en paramêtre:
 - l'url de retour précédamment encodée en utilisant la méthode: globaz.hera.external.ISFUrlEncode.encodeUrl("mon URL de retour")
 - idTiers du tiers dont on souhaite construire la situation familiale. Le tiers dévient requérant et est mis en session pour la constuction de la famille
 
Et appeler l'action...
/hera?userAction=hera.famille.apercuRelationFamilialeRequerant.entrerApplication&idTiers=xxxxx&urlFrom=yyyyyyy


3. HERA INTERFACE
******************

L'implémentation des interface se fait dans le package globaz.hera.impl .
Par défaut, l'implémentation du domaine d'application standard est appelée.
Pour faire varier le comportement d'une méthode en fonction d'un domaine d'application, surcharger la classe SFSituationFamilliale.

La classe mère pour extraire des informations depuis la Situation Familiale est globaz.hera.api.ISFSituationFamiliale.
En effet, tous les objets (BEntity) sont retournés par les getter des méthodes de cette classes.
Aucune famille ne peut être crée ou modifiée depuis l'interface.

UTILISATION DE L'INTERFACE:

BIApplication application = GlobazSystem.getApplication("HERA_REMOTE");
BSession session = (BSession) application.newSession("globazf", "ssiiadm");
ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_STANDARD);

//A partir d'une instance de ISFSituationFamiliale en fonction du domaine, 
//toutes les méthodes concerant la situation familale peuvent être appelées
ISFMembreFamilleRequerant[] result = sf.getMembresFamille("257667","12.12.1998");
