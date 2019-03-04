package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux droits
 * 
 * <ul>
 * <li>état du droit</li>
 * <li>motif de fin</li>
 * <li>motif de réduction</li>
 * <li>type d'allocation de naissance</li>
 * <li>statut familial</li>
 * <li>type de droit</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSDroit {
    /*
     * Etats des droits
     */
    /**
     * CS : état du droit "actif"
     */
    public static final String ETAT_A = "61090001";
    /**
     * CS : état du droit "inactif + calcul du rang"
     */
    public static final String ETAT_G = "61090002";
    /**
     * CS : état du droit "suspendu"
     */
    public static final String ETAT_S = "61090003";

    /*
     * ========================================================================= groupes de codes système
     * =========================================================================
     */

    /**
     * CS : groupe "état du droit"
     * 
     * @see ALCSDroit#ETAT_A
     * @see ALCSDroit#ETAT_G
     * @see ALCSDroit#ETAT_S
     */
    public static final String GROUP_ETAT = "60090000";
    /**
     * CS : groupe "motif de fin de droit"
     * 
     * @see ALCSDroit#MOTIF_FIN_ECH
     * @see ALCSDroit#MOTIF_FIN_RAD
     * @see ALCSDroit#MOTIF_FIN_CTAR
     */
    public static final String GROUP_MOTIF_FIN = "60100000";
    /**
     * CS : groupe "motif de réduction"
     * 
     * @see ALCSDroit#MOTIF_REDUC_COMP
     * @see ALCSDroit#MOTIF_REDUC_CONAC
     * @see ALCSDroit#MOTIF_REDUC_PAR
     */
    public static final String GROUP_MOTIF_REDUC = "60110000";
    /**
     * CS : groupe "type d'allocation de naissance"
     * 
     * @see ALCSDroit#NAISSANCE_TYPE_ACCE
     * @see ALCSDroit#NAISSANCE_TYPE_AUCUNE
     * @see ALCSDroit#NAISSANCE_TYPE_NAIS
     */
    public static final String GROUP_NAISSANCE_TYPE = "60130000";
    /**
     * CS : groupe "statut familial"
     * 
     * @see ALCSDroit#STATUT_FAMILIAL_ADOPTE
     * @see ALCSDroit#STATUT_FAMILIAL_DIVORCE
     * @see ALCSDroit#STATUT_FAMILIAL_ENFANT_DU_CONJ
     * @see ALCSDroit#STATUT_FAMILIAL_HORS_MARIAGE
     * @see ALCSDroit#STATUT_FAMILIAL_MARIAGE_ACTUEL
     * @see ALCSDroit#STATUT_FAMILIAL_MARIAGE_PRECEDENT
     * @see ALCSDroit#STATUT_FAMILIAL_RECUEILLI
     */
    // public static final String GROUP_STATUT_FAMILIAL = "60140000";
    /**
     * CS : groupe "type de droit"
     * 
     * @see ALCSDroit#TYPE_ACCE
     * @see ALCSDroit#TYPE_ENF
     * @see ALCSDroit#TYPE_ENF_LJA
     * @see ALCSDroit#TYPE_FNB
     * @see ALCSDroit#TYPE_FORM
     * @see ALCSDroit#TYPE_MEN
     * @see ALCSDroit#TYPE_MEN_LFA
     * @see ALCSDroit#TYPE_MEN_LJA
     * @see ALCSDroit#TYPE_NAIS
     */
    public static final String GROUP_TYPE = "60150000";

    /**
     * CS : motif de fin de droit "changement de tarif"
     */
    public static final String MOTIF_FIN_CTAR = "61100001";
    /**
     * CS : motif de fin de droit "droit  échu"
     */
    public static final String MOTIF_FIN_ECH = "61100002";
    /**
     * CS : motif de fin de droit "Radiation"
     */
    public static final String MOTIF_FIN_RAD = "61100003";
    /**
     * CS : motif de fin de droit spécifique E411
     */
    public static final String MOTIF_FIN_E411 = "61100010";

    /*
     * Motifs de réduction d'un droit
     */
    /**
     * CS : motif de réduction "complet"
     */
    public static final String MOTIF_REDUC_COMP = "61110001";
    /**
     * CS : motif de réduction "conjoint actif"
     */
    public static final String MOTIF_REDUC_CONAC = "61110002";
    /**
     * CS : motif de réduction "partiel"
     */
    public static final String MOTIF_REDUC_PAR = "61110003";

    /*
     * Type de l'allocation de naissance
     */
    /**
     * CS : type d'allocation de naissance accordé "accueil"
     */
    public static final String NAISSANCE_TYPE_ACCE = "61130001";
    /**
     * CS : type d'allocation de naissance accordé "aucune"
     */
    public static final String NAISSANCE_TYPE_AUCUNE = "61130002";
    /**
     * CS : type d'allocation de naissance accordé "naissance"
     */
    public static final String NAISSANCE_TYPE_NAIS = "61130003";

    /*
     * Type de prestation
     */
    /**
     * CS : type de droit "accueil"
     */
    public static final String TYPE_ACCE = "61150001";
    /**
     * CS : type de droit "enfant"
     */
    public static final String TYPE_ENF = "61150002";
    /**
     * CS : type de droit "enfant dans l'agriculture jurassienne"
     */
    public static final String TYPE_ENF_LJA = "61150006";
    /**
     * CS : type de droit "Famille nombreuse"
     */
    public static final String TYPE_FNB = "61150007";
    /**
     * CS : type de droit "formation"
     */
    public static final String TYPE_FORM = "61150003";
    /**
     * CS : type de droit "ménage"
     */
    public static final String TYPE_MEN = "61150004";
    /**
     * CS : type de droit "loi fédérale pour l'agriculture"
     */
    public static final String TYPE_MEN_LFA = "61150008";
    /**
     * CS : type de droit "loi jurassienne pour l'agriculture"
     */
    public static final String TYPE_MEN_LJA = "61150009";
    /**
     * CS : type de droit "naissance"
     */
    public static final String TYPE_NAIS = "61150005";
}
