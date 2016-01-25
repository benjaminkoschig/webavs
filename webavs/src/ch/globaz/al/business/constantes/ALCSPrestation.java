package ch.globaz.al.business.constantes;

/**
 * Codes système liés au prestations
 * 
 * <ul>
 * <li>codes d'erreur</li>
 * <li>état de la prestation</li>
 * <li>type de génération</li>
 * <li>type de paiement</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSPrestation {

    /*
     * Types de bonification
     */
    /**
     * CS : bonification "directe"
     */
    public static final String BONI_DIRECT = "61240001";
    /**
     * CS : bonification "indirecte"
     */
    public static final String BONI_INDIRECT = "61240002";
    /**
     * CS : bonification "restitution"
     */
    public static final String BONI_RESTITUTION = "61240003";

    /*
     * Etat des prestations
     */
    /**
     * CS : état "comptabilisé"
     */
    public static final String ETAT_CO = "61170001";
    /**
     * CS : état "provisoire"
     */
    public static final String ETAT_PR = "61170002";
    /**
     * CS : état "saisi"
     */
    public static final String ETAT_SA = "61170003";
    /**
     * CS : état "temporaire" (prestations travail pour dossier ADI)
     */
    public static final String ETAT_TMP = "61170005";
    /**
     * CS : état "transmis"
     */
    public static final String ETAT_TR = "61170004";

    /*
     * Mode de génération des prestation
     */
    /**
     * CS : type de génération "versement"
     */
    public static final String GENERATION_TYPE_GEN_AFFILIE = "61180002";
    /**
     * CS : type de génération "versement"
     */
    public static final String GENERATION_TYPE_GEN_DOSSIER = "61180001";
    /**
     * CS : type de génération "versement"
     */
    public static final String GENERATION_TYPE_GEN_GLOBALE = "61180003";

    /*
     * ========================================================================= groupes de codes système
     * =========================================================================
     */

    /**
     * CS : groupe "bonifications"
     * 
     * @see ALCSPrestation#BONI_DIRECT
     * @see ALCSPrestation#BONI_INDIRECT
     * @see ALCSPrestation#BONI_RESTITUTION
     */
    public static final String GROUP_BONI = "60240000";

    /**
     * CS : groupe "état de prestation"
     * 
     * @see ALCSPrestation#ETAT_CO
     * @see ALCSPrestation#ETAT_PR
     * @see ALCSPrestation#ETAT_SA
     * @see ALCSPrestation#ETAT_TR
     */
    public static final String GROUP_ETAT = "60170000";
    /**
     * CS : groupe "type de génération"
     * 
     * @see ALCSPrestation#GENERATION_TYPE_GEN_AFFILIE
     * @see ALCSPrestation#GENERATION_TYPE_GEN_DOSSIER
     * @see ALCSPrestation#GENERATION_TYPE_GEN_GLOBALE
     */
    public static final String GROUP_GENERATION_TYPE = "60180000";

    /**
     * CS : groupe "statut de prestation"
     * 
     * @see ALCSPrestation#STATUT_ADC
     * @see ALCSPrestation#STATUT_ADI
     * @see ALCSPrestation#STATUT_CH
     */
    public static final String GROUP_STATUT = "60230000";
    /*
     * =========================================================================
     * =========================================================================
     */

    /*
     * Statut de prestation
     */
    /**
     * CS : statut de prestation "ADC"
     */
    public static final String STATUT_ADC = "61230002";
    /**
     * CS : statut de prestation "ADI"
     */
    public static final String STATUT_ADI = "61230003";
    /**
     * CS : statut de prestation "Suisse"
     */
    public static final String STATUT_CH = "61230001";

}
