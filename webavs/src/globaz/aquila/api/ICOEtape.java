/*
 * Créé le 2 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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

    public final static String CS_1ER_RAPPEL_DE_PV_DE_SAISIE_ENVOYE = "5200011";

    public final static String CS_1ER_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200020";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public final static String CS_1ER_RAPPEL_DE_SAISIE_ENVOYE = "5200016";
    public final static String CS_1ERE_LETTRE_DE_RAPPEL_DE_COMMANDEMENT_DE_PAYER_ENVOYE = "5200002";
    public final static String CS_2EME_LETTRE_DE_RAPPEL_DE_COMMANDEMENT_DE_PAYER_ENVOYE = "5200003";
    public final static String CS_2EME_RAPPEL_DE_PV_DE_SAISIE_ENVOYE = "5200012";
    public final static String CS_2EME_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200021";
    public final static String CS_2EME_RAPPEL_DE_SAISIE_ENVOYE = "5200017";
    public final static String CS_3EME_LETTRE_DE_RAPPEL_DE_COMMANDEMENT_DE_PAYER_ENVOYE = "5200004";
    public final static String CS_3EME_RAPPEL_DE_PV_DE_SAISIE_ENVOYE = "5200013";
    public final static String CS_3EME_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200022";
    public final static String CS_3EME_RAPPEL_DE_SAISIE_ENVOYE = "5200018";
    public final static String CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI = "5200023";
    public final static String CS_ARD_CREE = "5200029";
    public final static String CS_AUCUNE = "5200030";
    public final static String CS_AVIS_VENTE_AUX_ENCHERES_SAISI = "5200040";
    public final static String CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION = "5200031";
    public final static String CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION = "5200005";
    public final static String CS_CONTENTIEUX_CREE = "5200035";
    public final static String CS_DECISION = "5200044";
    public final static String CS_DEMANDE_DE_MAINLEVEE_ENVOYEE = "5200008";
    public final static String CS_DEMANDE_DE_RETRAIT_D_OPPOSITION_ENVOYE = "5200006";
    public final static String CS_RECEPTION_RETRAIT_D_OPPOSITION_SAISIE = "5200048";
    public final static String CS_DEMANDE_DE_RETRAIT_DE_LA_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYE = "5200025";
    public final static String CS_DEUXIEME_RAPPEL_ENVOYE = "5200033";
    public final static String CS_ETAPE_MANUELLE = "5200047";
    public final static String CS_FRAIS_ET_INTERETS_RECLAMES = "5200007";
    public final static String CS_IRRECOUVRABLE = "5200024";
    public final static String CS_JUGEMENT_DE_MAINLEVEE_SAISI = "5200009";
    public final static String CS_POURSUITE_CLASSEE = "5200026";
    public final static String CS_POURSUITE_RADIEE = "5200027";
    public final static String CS_PREMIER_RAPPEL_ENVOYE = "5200032";
    public final static String CS_PV_DE_SAISIE_SAISI = "5200041";
    public final static String CS_PV_DE_SAISIE_SUR_BIENS_IMMOBILIERS_SAISI = "5200015";
    public final static String CS_PV_DE_SAISIE_SUR_BIENS_MOBILIERS_SAISI = "5200037";
    public final static String CS_PV_DE_SAISIE_SUR_SALAIRE_SAISI = "5200014";
    public final static String CS_PV_SAISIE_VALANT_ADB_SAISI = "5200039";
    public final static String CS_RAPPEL_DE_RECLAMATION_DE_FRAIS_ET_INTERETS_ENVOYE = "5200028";
    public final static String CS_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE = "5200045";
    public final static String CS_RAPPEL_SURSIS_VENTE_NON_REPECTE = "5200046";
    public final static String CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE = "5200010";
    public final static String CS_REQUISITION_DE_POURSUITE_ENVOYEE = "5200001";
    public final static String CS_REQUISITION_DE_VENTE_SAISIE = "5200019";
    public final static String CS_RETRAIT_REQUISITION_VENTE = "5200043";
    public final static String CS_SOMMATION_ENVOYEE = "5200034";
    public final static String CS_SURSIS_DE_L_ARTICLE_123LP_SAISI = "5200038";
    // Type étape
    public final static String CS_TYPE_SPECIFIQUE = "5220001";
    public final static String CS_VERSEMENT_IMPUTE = "5200036";
    /** critère pour rechercher une Etape selon son identifiant. */
    public static final String FOR_ID_ETAPE = "forIdEtape";

    /** critère pour rechercher les étapes pour une séquence en particulier. */
    public static final String FOR_ID_SEQUENCE = "forIdSequence";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne le code système du libellé de l'action.
     * 
     * @return DOCUMENT ME!
     */
    public String getCsAction();

    /**
     * retourne le code système du libellé de l'étape.
     * 
     * @return DOCUMENT ME!
     */
    public String getCsEtape();

    /**
     * retourne l'identifiant de l'etape.
     * 
     * @return DOCUMENT ME!
     */
    public String getIdEtape();

    /**
     * retourne l'identifiant de la sequence qui contient cette etape.
     * 
     * @return DOCUMENT ME!
     */
    public String getIdSequence();

    /**
     * charge l'ensemble des etapes qui correspondent à une série de critères (utiliser les constantes définies dans
     * cette classe.
     * 
     * @param criteres
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Collection /* ICOEtape */load(HashMap criteres) throws Exception;

    /**
     * Cette méthode permet de déterminer si une étape concerne la poursuite (Etape réquisition de poursuite et
     * suivante) ou non True = RP et ss false = étape précédent la RP (par exemple rappel, sommation, décision)
     * 
     * @param CS_Etape
     * @return boolean
     */
    // public boolean isEtapePoursuite(String CS_Etape);
}
