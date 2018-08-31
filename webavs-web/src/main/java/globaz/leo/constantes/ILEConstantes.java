/*
 * Globaz SA.
 * Créé le 5 avr. 05
 */
package globaz.leo.constantes;

import globaz.envoi.constantes.ENConstantes;

public interface ILEConstantes extends ENConstantes {
    String ACTION_RECU = "reçu";
    String CODES_SYSTEMES_ID = "PCOSID";
    String CODES_SYSTEMES_TABLE = "FWCOSP";
    String CS_ALLEMAND = "6100002";
    /************************************************************/
    /******* Catégorie des envois *******************************/

    String CS_CATEGORIE_GROUPE = "16000007";
    String CS_CATEGORIE_RADIATION = "6700007";
    String CS_CATEGORIE_SUIVI_BTA = "6700010";
    String CS_CATEGORIE_SUIVI_BULLETIN_NEUTRE = "6700011";
    String CS_CATEGORIE_SUIVI_DS = "6700003";
    String CS_CATEGORIE_SUIVI_DS_LTN = "6700009";
    String CS_CATEGORIE_SUIVI_DS_STRUCTURE = "6700012";
    String CS_CATEGORIE_SUIVI_LAA = "6700001";
    String CS_CATEGORIE_SUIVI_LPP = "6700002";
    String CS_CATEGORIE_SUIVI_LPP_ANNUEL = "6700013";
    String CS_CATEGORIE_SUIVI_RELEVES = "6700005";
    String CS_CATEGORIE_SUIVI_SALAIRES = "6700004";
    String CS_CATEGORIES_NOUVELLE_AFFILIATION = "6700006";
    String CS_CATEGORIE_NON_CERTIFIES_CONFORMES = "6700014";

    /************************************************************/
    /******** DATES *******************************************/
    String CS_DATE_DEBUT = "6701003";
    String CS_DATE_FIN = "6701004";
    /******* Définition de formule Nouvelle Affiliation Rétroactive *******/
    String CS_DEBUT_AFFILIATION_RETROACTIVE = "6200042";
    String CS_DEBUT_AFFILIATION_RETROACTIVE_FIN = "6200045";
    String CS_DEBUT_AFFILIATION_RETROACTIVE_RAPPEL = "6200043";
    String CS_DEBUT_AFFILIATION_RETROACTIVE_RAPPEL2 = "6200044";
    /******* Définition de formule Nouvelle Affiliation ********/
    String CS_DEBUT_NOUVELLE_AFFILIATION = "6200034";
    /******* Définition de formule Radiation ********/
    String CS_DEBUT_RADIATION = "6200038";
    /******* Définition de formule Radiation Affiliation Rétroactive *******/
    String CS_DEBUT_RADIATION_RETROACTIVE = "6200046";
    String CS_DEBUT_RADIATION_RETROACTIVE_FIN = "6200049";
    String CS_DEBUT_RADIATION_RETROACTIVE_RAPPEL = "6200047";
    String CS_DEBUT_RADIATION_RETROACTIVE_RAPPEL2 = "6200048";
    /******* Définition de formule annonce salaires ********/
    String CS_DEBUT_SUIVI_ANNONCE_SALAIRES = "6200023";
    String CS_DEBUT_SUIVI_ATTESTATION_IP = "6200014";
    /******* Définition de formule BTA *******/
    String CS_DEBUT_SUIVI_BTA = "6200057";
    /******* Définition de formule BULLETIN NEUTRE *******/
    String CS_DEBUT_SUIVI_BULLETIN_NEUTRE = "6200061";
    /******* Définition de formule confirmation salaires ********/
    String CS_DEBUT_SUIVI_CONFIRMATION_SALAIRES = "6200027";
    /******* Définition de formule DS ********/
    String CS_DEBUT_SUIVI_DS = "6200016";
    /******* Définition de formule DS LTN *******/
    String CS_DEBUT_SUIVI_DS_LTN = "6200052";
    String CS_DEBUT_SUIVI_LAA = "6200001";
    /*************************************/
    /******* Définition de formule LPP ********/
    String CS_DEBUT_SUIVI_LPP = "6200007";
    /******* Définition de formule relevés ********/
    String CS_DEBUT_SUIVI_RELEVES = "6200029";
    String CS_DEF_FORMULE_AMENDE_DS = "6200073";
    String CS_DEF_FORMULE_AMENDE_LPP = "6200034";
    String CS_DEF_FORMULE_ANNONCE_SALAIRES = "6200024";
    String CS_DEF_FORMULE_ANNONCE_SALAIRES_RAP = "6200025";
    String CS_DEF_FORMULE_ANNONCE_SALAIRES_SOM = "6200026";
    String CS_DEF_FORMULE_ATT_DS = "6200017";
    String CS_DEF_FORMULE_ATT_DS_LTN = "6200056";
    String CS_DEF_FORMULE_ATTEST_IP = "6200015";
    String CS_DEF_FORMULE_BN_IMPRESSION = "6200062";
    String CS_DEF_FORMULE_BN_SOMMATION = "6200063";
    String CS_DEF_FORMULE_BN_TRAITEMENT_SOMMATION_ECHUE = "6200064";
    String CS_DEF_FORMULE_CONFIRMATION_SALAIRES = "6200028";
    String CS_DEF_FORMULE_DENONCIATION_LAA = "6200005";
    String CS_DEF_FORMULE_DENONCIATION_LAA_TSE = "8360022";
    String CS_DEF_FORMULE_EDIT_ANNONCE_LPP = "6200011";
    /************************************/
    /******* Definition formule LAA *****/
    String CS_DEF_FORMULE_GROUPE = "16000002";
    String CS_DEF_FORMULE_IMPR_ANNONCE_LPP = "6200013";
    String CS_DEF_FORMULE_MANDAT_AGENT_DS = "6200065";
    String CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL1_DS = "6200066";
    String CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL2_DS = "6200067";
    String CS_DEF_FORMULE_PLAINTE_DS = "6200021";
    String CS_DEF_FORMULE_QUEST_LAA = "6200002";
    String CS_DEF_FORMULE_QUEST_LPP = "6200008";
    String CS_DEF_FORMULE_RAPPEL_DS = "6200018";
    String CS_DEF_FORMULE_RAPPEL_DS_LTN = "6200053";
    String CS_DEF_FORMULE_RAPPEL_LAA = "6200003";
    String CS_DEF_FORMULE_RAPPEL_LPP = "6200009";
    /*************************************/
    String CS_DEF_FORMULE_RECEPTION = "6200006";
    String CS_DEF_FORMULE_SAISI_ANNONCE_LPP = "6200012";
    String CS_DEF_FORMULE_SOMMATION_DS = "6200019";
    String CS_DEF_FORMULE_SOMMATION_DS_LTN = "6200054";
    String CS_DEF_FORMULE_SOMMATION_LAA = "6200004";
    String CS_DEF_FORMULE_SOMMATION_LPP = "6200010";
    String CS_DEF_FORMULE_TAXATION_DS = "6200022";
    String CS_DEF_FORMULE_TRAITER_SOMMATION_DS_LTN = "6200055";
    String CS_DS_ST_COMTROLE_EMPLOYEUR = "6200072";
    /******* Définition de formule DECLARATION STRUCTUREE *******/
    String CS_DS_ST_DEBUT_SUIVI = "6200068";
    String CS_DS_ST_ENVOI = "6200069";
    String CS_DS_ST_RAPPEL = "6200070";
    String CS_DS_ST_SOMMATION = "6200071";
    /************************************************************/
    /******* statut des envois **********************************/
    String CS_EDITION_MANUELLE_GROUPE = "16000009";
    /************************************************************/
    /******* étape suivante *************************************/
    String CS_ETAPE_SUIVANTE_GROUPE = "16000008";
    String CS_FIN_SUIVI_RELEVES = "6200033";
    /******************* LES LANGUES ****************************/
    String CS_FRANCAIS = "6100001";
    /************************************************************/
    /******* Groupe de CS **********************************/
    String CS_GROUPE_CHOIX = "16000010";

    /************************************************************/
    /******* Groupe de CS **********************************/
    String CS_GROUPE_DATE = "16000011";
    /************************************************************/
    /******* Groupe de CS **********************************/
    String CS_GROUPE_TYPE_CHAMP = "16000012";
    String CS_GROUPE_TYPE_CHOIX = "7000001";
    String CS_GROUPE_TYPE_DATE = "7000002";
    String CS_GROUPE_TYPE_TIERS = "7000003";
    String CS_IMPRESSION_REPETITION_BTA = "6200058";
    /************************************************************/
    /****************** IS FORMULE DEBUT OU FIN *******************/
    String CS_IS_DEB_OR_FIN = "16000013";
    String CS_LOT_TYPE_AMENDE_DEC_SAL = "7400002";
    /************************************************************/
    String CS_LOT_TYPE_ETAPESUIVANTE = "7400001";
    String CS_NON = "6300002";
    String CS_NOUVELLE_AFFILIATION_FIN = "6200037";
    String CS_NOUVELLE_AFFILIATION_RAPPEL = "6200035";
    String CS_NOUVELLE_AFFILIATION_RAPPEL2 = "6200036";
    /******* Code system ****************/
    String CS_OUI = "6300001";
    /*****************************************/
    /******* Parametre de génération des documents AVS *********/
    String CS_PARAM_GEN_GROUP = "16000006";
    String CS_PARAM_GEN_ID_AFFILIATION = "6600007";
    String CS_PARAM_GEN_ID_ENVOI_PRECEDENT = "6600005";
    String CS_PARAM_GEN_ID_PLAN_AFFILIATION = "6600008";
    String CS_PARAM_GEN_ID_TIERS = "6600001";
    String CS_PARAM_GEN_ID_TIERS_DESTINAIRE = "6600006";
    String CS_PARAM_GEN_NUMERO = "6600002";
    String CS_PARAM_GEN_PERIODE = "6600009";
    String CS_PARAM_GEN_PLAN = "6600010";
    String CS_PARAM_GEN_ROLE = "6600003";

    String CS_PARAM_GEN_TYPE_PROVENANCE_MODULE = "6600004";
    /******* Provenance de l'envoi *********/
    String CS_PROVENANCE_ID_ENVOI = "6500002";
    String CS_RADIATION_FIN = "6200041";
    String CS_RADIATION_RAPPEL = "6200039";
    String CS_RADIATION_RAPPEL2 = "6200040";
    String CS_RAPPEL_REPETITION_BTA = "6200059";
    String CS_RELEVES_IMPRESSION = "6200050";
    String CS_RELEVES_RAPPEL = "6200030";
    String CS_RELEVES_SOMMATIOM = "6200031";
    String CS_RELEVES_TAXATION_OFFICE = "6200032";
    String CS_TRAITER_RAPPEL_NON_RECU_BTA = "6200060";
    String CS_TYPEFORMULEPDF = "6400002";
    String LABEL = "label";

    String NOM_GROUPE_LANGUE = "LELANGUE";
    String VALUE = "value";

    /******* Définition de formule LPP ********/
    String CS_DEBUT_SUIVI_ANNUEL_LPP = "6200074";
    String CS_QUESTIONNAIRE_ANNUEL_LPP = "6200075";
    String CS_DEF_FORMULE_RAPPEL_ANNUEL_LPP = "6200076";
    String CS_DEF_FORMULE_SOMMATION_ANNUEL_LPP = "6200077";
    String CS_DEF_FORMULE_DENONCIATION_ANNUEL_LPP = "6200078";

    /******* Définition de formule LPP ********/
    String CS_DEBUT_SUIVI_NCC = "6200079";

}
