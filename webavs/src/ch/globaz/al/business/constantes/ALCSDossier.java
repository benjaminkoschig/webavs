package ch.globaz.al.business.constantes;

/**
 * Codes syst�me li�s aux dossiers
 * 
 * <ul>
 * <li>activit�</li>
 * <li>type de commentaires</li>
 * <li>�tat du dossier</li>
 * <li>motif de r�duction du dossier</li>
 * <li>statut du dossier</li>
 * <li>unit� de calcul</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSDossier {

    /**
     * CS : activit� "Agriculteur"
     */
    public static final String ACTIVITE_AGRICULTEUR = "61040001";
    /**
     * CS : activit� "Collaborateur Agricole"
     */
    public static final String ACTIVITE_COLLAB_AGRICOLE = "61040002";
    /**
     * CS : activit� "Exploitant d'alapage"
     */
    public static final String ACTIVITE_EXPLOITANT_ALPAGE = "61040012";
    /**
     * CS : activit� "Ind�pendant"
     */
    public static final String ACTIVITE_INDEPENDANT = "61040003";
    /**
     * CS : activit� "non-actif"
     */
    public static final String ACTIVITE_NONACTIF = "61040004";
    /**
     * CS : activit� "P�cheur"
     */
    public static final String ACTIVITE_PECHEUR = "61040009";
    /**
     * CS : activit� "Salari�"
     */
    public static final String ACTIVITE_SALARIE = "61040005";
    /**
     * CS : activit� "travailleur agricole"
     */
    public static final String ACTIVITE_TRAVAILLEUR_AGRICOLE = "61040006";
    /**
     * CS : activit� "Travailleur sans employeur"
     */
    public static final String ACTIVITE_TSE = "61040010";
    /**
     * CS : activit� "vigneron"
     */
    public static final String ACTIVITE_VIGNERON = "61040011";

    /*
     * Types commentaires
     */
    /**
     * CS : type de commentaire "d�cision"
     */
    public static final String COMMENTAIRE_TYPE_DECISION = "61030001";

    /**
     * CS : type de commentaire "dossier"
     */
    public static final String COMMENTAIRE_TYPE_DOSSIER = "61030002";
    /**
     * CS : compl�ment activit� "mission temporaire"
     */
    public static final String COMPLEMENT_ACTIVITE_MISSION_TEMP = "61140001";

    /*
     * Etats des dossiers
     */

    /**
     * CS : �tat de dossier "actif"
     */
    public static final String ETAT_ACTIF = "61050001";
    /**
     * CS : �tat de dossier "complet"
     */
    public static final String ETAT_COMPLET = "61050006";

    /**
     * CS : �tat de dossier "en constitution"
     */
    public static final String ETAT_EN_CONSTITUTION = "61050005";
    /**
     * CS : �tat de dossier "radi�"
     */
    public static final String ETAT_RADIE = "61050002";
    /**
     * CS : �tat de dossier "refus�"
     */
    public static final String ETAT_REFUSE = "61050003";
    /**
     * CS : �tat de dossier "suspendu"
     */
    public static final String ETAT_SUSPENDU = "61050004";
    /*
     * Genre d'administration
     */
    /**
     * CS : genre administration "caisse AF"
     */
    public static final String GENRE_CAISSE_AF = "509030";
    /*
     * ========================================================================= groupes de codes syst�me
     * =========================================================================
     */
    /**
     * CS : groupe "activit�s"
     * 
     * @see ALCSDossier#ACTIVITE_AGRICULTEUR
     * @see ALCSDossier#ACTIVITE_COLLAB_AGRICOLE
     * @see ALCSDossier#ACTIVITE_INDEPENDANT
     * @see ALCSDossier#ACTIVITE_NONACTIF
     * @see ALCSDossier#ACTIVITE_PECHEUR
     * @see ALCSDossier#ACTIVITE_SALARIE
     * @see ALCSDossier#ACTIVITE_TRAVAILLEUR_AGRICOLE
     * @see ALCSDossier#ACTIVITE_TSE
     */
    public static final String GROUP_ACTIVITE_ALLOC = "60040000";

    /**
     * CS : groupe "type de commentaires"
     * 
     * @see ALCSDossier#COMMENTAIRE_TYPE_DECISION
     * @see ALCSDossier#COMMENTAIRE_TYPE_DOSSIER
     */
    public static final String GROUP_COMMENTAIRE_TYPE = "60030000";
    /**
     * CS : groupe "compl�ment activit�s"
     * 
     * @see ALCSDossier#COMPLEMENT_ACTIVITE_MISSION_TEMP
     */
    public static final String GROUP_COMPLEMENT_ACTIVITE = "60140000";
    /**
     * CS : groupe "�tat de dossier"
     * 
     * @see ALCSDossier#ETAT_ACTIF
     * @see ALCSDossier#ETAT_RADIE
     * @see ALCSDossier#ETAT_REFUSE
     * @see ALCSDossier#ETAT_SUSPENDU
     */
    public static final String GROUP_ETAT = "60050000";
    /**
     * CS : groupe "lien entre dossiers"
     * 
     * @see ALCSDossier#LIEN_ALLOC
     * @see ALCSDossier#LIEN_CONJOINT
     */
    public static final String GROUP_LIEN_DOSSIER = "60120000";
    /**
     * CS : groupe "motif de r�duction"
     * 
     * @see ALCSDossier#MOTIF_REDUC_COMP
     * @see ALCSDossier#MOTIF_REDUC_CONAC
     * @see ALCSDossier#MOTIF_REDUC_PAR
     */
    public static final String GROUP_MOTIF_REDUC = "60060000";

    /**
     * CS : groupe "type de paiement"
     * 
     * @see ALCSDossier#PAIEMENT_DIRECT
     * @see ALCSDossier#PAIEMENT_INDIRECT
     * @see ALCSDossier#PAIEMENT_TIERS
     */
    public static final String GROUP_PAIEMENT = "61250000";

    /**
     * CS : groupe "statut du dossier"
     * 
     * @see ALCSDossier#STATUT_CP
     * @see ALCSDossier#STATUT_CS
     * @see ALCSDossier#STATUT_IP
     * @see ALCSDossier#STATUT_IS
     * @see ALCSDossier#STATUT_N
     * @see ALCSDossier#STATUT_NP
     * 
     */
    public static final String GROUP_STATUT = "60070000";
    /**
     * CS : groupe "unit� de calcul"
     * 
     * @see ALCSDossier#UNITE_CALCUL_HEURE
     * @see ALCSDossier#UNITE_CALCUL_JOUR
     * @see ALCSDossier#UNITE_CALCUL_MOIS
     * @see ALCSDossier#UNITE_CALCUL_SPECIAL
     */
    public static final String GROUP_UNITE_CALCUL = "60080000";

    /*
     * =========================================================================
     * =========================================================================
     */

    /*
     * Type de liens dossiers
     */
    /**
     * CS : lien dossier de type allocataire
     */
    public static final String LIEN_ALLOC = "61120001";
    /**
     * CS : lien dossier de type conjoint
     */
    public static final String LIEN_CONJOINT = "61120002";

    /*
     * Motifs de r�duction d'un dossier
     */
    /**
     * CS : motif de r�duction "complet"
     */
    public static final String MOTIF_REDUC_COMP = "61060001";

    /**
     * CS : motif de r�duction "conjoint actif"
     */
    public static final String MOTIF_REDUC_CONAC = "61060002";
    /**
     * CS : motif de r�duction "partiel"
     */
    public static final String MOTIF_REDUC_PAR = "61060003";
    /*
     * Mode de paiement (indirect, direct allocataire ou direct tiers b�n�ficiaire)
     */
    /**
     * CS : mode de paiement "direct"
     */
    public static final String PAIEMENT_DIRECT = "61250001";
    /**
     * CS : mode de paiement "indirect"
     */
    public static final String PAIEMENT_INDIRECT = "61250002";
    /**
     * CS : mode de paiement "tiers"
     */
    public static final String PAIEMENT_TIERS = "61250003";

    /*
     * Statut d'un dossier
     */
    /**
     * CS : statut du dossier "Cantonal prioritaire"
     */
    public static final String STATUT_CP = "61070001";
    /**
     * CS : statut du dossier "Cantonal suppl�tif"
     */
    public static final String STATUT_CS = "61070002";
    /**
     * CS : statut du dossier "International prioritaire"
     */
    public static final String STATUT_IP = "61070003";
    /**
     * CS : statut du dossier "International suppl�tif"
     */
    public static final String STATUT_IS = "61070004";

    /**
     * CS : statut du dossier "Normal"
     */
    public static final String STATUT_N = "61070005";
    /**
     * CS : statut du dossier "Normal prioritaire"
     */
    public static final String STATUT_NP = "61070006";
    /*
     * Unit� de calcul
     */
    /**
     * CS : unit� "horaire"
     */
    public static final String UNITE_CALCUL_HEURE = "61080001";
    /**
     * CS : unit� "journalier"
     */
    public static final String UNITE_CALCUL_JOUR = "61080002";

    /*
     * Type de lien entre dossiers
     */

    /**
     * CS : unit� "mensuel"
     */
    public static final String UNITE_CALCUL_MOIS = "61080003";
    /**
     * CS : unit� "sp�cial". Utilis� pour les prestation de naissance et les ADI
     */
    public static final String UNITE_CALCUL_SPECIAL = "61080004";

}
