/*
 * Cr?? le 2 f?vr. 06
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api;

import java.util.Collection;
import java.util.HashMap;

/**
 * <h1>Description</h1> .
 * 
 * @author vre
 */
public interface ICOEtape {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    String CS_1ER_RAPPEL_DE_PV_DE_SAISIE_ENVOYE = "5200011";

    String CS_1ER_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200020";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    String CS_1ER_RAPPEL_DE_SAISIE_ENVOYE = "5200016";
    String CS_1ERE_LETTRE_DE_RAPPEL_DE_COMMANDEMENT_DE_PAYER_ENVOYE = "5200002";
    String CS_2EME_LETTRE_DE_RAPPEL_DE_COMMANDEMENT_DE_PAYER_ENVOYE = "5200003";
    String CS_2EME_RAPPEL_DE_PV_DE_SAISIE_ENVOYE = "5200012";
    String CS_2EME_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200021";
    String CS_2EME_RAPPEL_DE_SAISIE_ENVOYE = "5200017";
    String CS_3EME_LETTRE_DE_RAPPEL_DE_COMMANDEMENT_DE_PAYER_ENVOYE = "5200004";
    String CS_3EME_RAPPEL_DE_PV_DE_SAISIE_ENVOYE = "5200013";
    String CS_3EME_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200022";
    String CS_3EME_RAPPEL_DE_SAISIE_ENVOYE = "5200018";
    String CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI = "5200023";
    String CS_ARD_CREE = "5200029";
    String CS_AUCUNE = "5200030";
    String CS_AVIS_VENTE_AUX_ENCHERES_SAISI = "5200040";
    String CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION = "5200031";
    String CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION = "5200005";
    String CS_CONTENTIEUX_CREE = "5200035";
    String CS_DECISION = "5200044";
    String CS_DEMANDE_DE_MAINLEVEE_ENVOYEE = "5200008";
    String CS_DEMANDE_DE_RETRAIT_D_OPPOSITION_ENVOYE = "5200006";
    String CS_RECEPTION_RETRAIT_D_OPPOSITION_SAISIE = "5200048";
    String CS_DEMANDE_DE_RETRAIT_DE_LA_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYE = "5200025";
    String CS_DEUXIEME_RAPPEL_ENVOYE = "5200033";
    String CS_ETAPE_MANUELLE = "5200047";
    String CS_FRAIS_ET_INTERETS_RECLAMES = "5200007";
    String CS_IRRECOUVRABLE = "5200024";
    String CS_JUGEMENT_DE_MAINLEVEE_SAISI = "5200009";
    String CS_POURSUITE_CLASSEE = "5200026";
    String CS_POURSUITE_RADIEE = "5200027";
    String CS_PREMIER_RAPPEL_ENVOYE = "5200032";
    String CS_PV_DE_SAISIE_SAISI = "5200041";
    String CS_PV_DE_SAISIE_SUR_BIENS_IMMOBILIERS_SAISI = "5200015";
    String CS_PV_DE_SAISIE_SUR_BIENS_MOBILIERS_SAISI = "5200037";
    String CS_PV_DE_SAISIE_SUR_SALAIRE_SAISI = "5200014";
    String CS_PV_SAISIE_VALANT_ADB_SAISI = "5200039";
    String CS_RAPPEL_DE_RECLAMATION_DE_FRAIS_ET_INTERETS_ENVOYE = "5200028";
    String CS_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200045";
    String CS_RAPPEL_SURSIS_VENTE_NON_REPECTE = "5200046";
    String CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE = "5200010";
    String CS_REQUISITION_DE_POURSUITE_ENVOYEE = "5200001";
    String CS_REQUISITION_DE_VENTE_SAISIE = "5200019";
    String CS_RETRAIT_REQUISITION_VENTE = "5200043";
    String CS_SOMMATION_ENVOYEE = "5200034";
    String CS_SURSIS_DE_L_ARTICLE_123LP_SAISI = "5200038";
    // Type ?tape
    String CS_TYPE_SPECIFIQUE = "5220001";
    String CS_VERSEMENT_IMPUTE = "5200036";
    /** crit?re pour rechercher une Etape selon son identifiant. */
    String FOR_ID_ETAPE = "forIdEtape";

    /** crit?re pour rechercher les ?tapes pour une s?quence en particulier. */
    String FOR_ID_SEQUENCE = "forIdSequence";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne le code syst?me du libell? de l'action.
     * 
     * @return DOCUMENT ME!
     */
    String getCsAction();

    /**
     * retourne le code syst?me du libell? de l'?tape.
     * 
     * @return DOCUMENT ME!
     */
    String getCsEtape();

    /**
     * retourne l'identifiant de l'etape.
     * 
     * @return DOCUMENT ME!
     */
    String getIdEtape();

    /**
     * retourne l'identifiant de la sequence qui contient cette etape.
     * 
     * @return DOCUMENT ME!
     */
    String getIdSequence();

    /**
     * charge l'ensemble des etapes qui correspondent ? une s?rie de crit?res (utiliser les constantes d?finies dans
     * cette classe.
     * 
     * @param criteres
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    Collection /* ICOEtape */load(HashMap criteres) throws Exception;

    /**
     * Cette m?thode permet de d?terminer si une ?tape concerne la poursuite (Etape r?quisition de poursuite et
     * suivante) ou non True = RP et ss false = ?tape pr?c?dent la RP (par exemple rappel, sommation, d?cision)
     * 
     * @param CS_Etape
     * @return boolean
     */
    // public boolean isEtapePoursuite(String CS_Etape);
}
