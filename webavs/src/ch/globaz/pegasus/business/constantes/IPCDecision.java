/**
 * 
 */
package ch.globaz.pegasus.business.constantes;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public interface IPCDecision {

    public final static String ACTION_IMPRIMER = "imprime";
    public final static String ACTION_PREVALIDATION = "prevalid";
    public final static String ACTION_VALIDATION = "valid";

    public final static String ANNEXE_BILLAG_AUTO = "64054001";
    public final static String ANNEXE_COPIE_MANUEL = "64054002";
    public final static String ANNEXE_COPIE_NON_EDITABLE = "64054003";

    // Annexes a gérer automatiquement
    public final static String BILLAG_ANNEXES_STRING = "JSP_PC_DECALCUL_D_BILLAG";
    public final static String CARTE_LEGITIMATION_ANNEXES_STRING = "JSP_PC_DECALCUL_D_CARTE_LEGITIM";
    public final static String CS_ETAT_DECISION_VALIDE = "64028003";
    public final static String CS_ETAT_ENREGISTRE = "64028001";

    public final static String CS_ETAT_PRE_VALIDE = "64028002";
    public final static String CS_GENRE_DECISION = "64048001";
    public final static String CS_GENRE_PROFORMA = "64048002";

    // Code systemes des motifs de refus et suppression contenenant un sous
    // motifs
    public final static String CS_MOTIF_SUPP_TEXTE_LIBRE = "64045019";
    public final static String CS_MOTIF_REFUS_WITH_SM = "64043012";
    public final static String CS_MOTIF_SUPPR_WITH_SM_1 = "64045007";
    public final static String CS_MOTIF_SUPPR_WITH_SM_2 = "64045008";

    public final static String CS_MOTIF_SUPPRESSION_DECES = "64045015";
    public final static String CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER = "64045014";
    public final static String CS_MOIF_REFUS_RENONCIATION = "64043013";
    // motifs transfert
    public final static String CS_MOTIF_TRANSFERT_SUPPRESSION_MAINLEVEE_TUTELLE = "64064002";
    public final static String CS_MOTIF_TRANSFERT_SUPPRESSION_NOMINATION_NOUVEAU_TUTEUR = "64064003";
    public final static String CS_MOTIF_TRANSFERT_SUPPRESSION_TRANSFERT_DOMICILE = "64064001";

    public final static String CS_PREP_COURANT = "64053002";

    public final static String CS_PREP_RETRO = "64053003";
    public final static String CS_PREP_STANDARD = "64053001";

    public final static String CS_TYPE_ADAPTATION_AC = "64042006";
    public final static String CS_TYPE_OCTROI_AC = "64042003";
    // etat des décision
    public final static String CS_TYPE_PARTIEL_AC = "64042005";
    public final static String CS_TYPE_REFUS_AC = "64042004";
    // Type des décision
    public final static String CS_TYPE_REFUS_SC = "64042001";

    public final static String CS_TYPE_SUPPRESSION_SC = "64042002";
    public final static String CS_VALIDE = "64028003";
    public final static String EXOCHIENS_ANNEXES_STRING = "JSP_PC_DECALCUL_D_EXOCHIENS";
    public final static String EXOTELE_ANNEXES_STRING = "JSP_PC_DECALCUL_D_EXOTELE";
    // public final static String GUIDE_REDUCTION_CARTE_LEGITIMATION_ANNEXES_STRING =
    // "JSP_PC_DECALCUL_D_GUIDE_CARTE_LEGITIM";

    public final static String NOTICE_ANNEXES_STRING = "JSP_PC_DECALCUL_D_NOTICE";

}
