package ch.globaz.al.business.constantes;

/**
 * Codes syst�me li�s au prestations
 * 
 * <ul>
 * <li>codes d'erreur</li>
 * <li>�tat de la prestation</li>
 * <li>type de g�n�ration</li>
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
     * CS : �tat "comptabilis�"
     */
    public static final String ETAT_CO = "61170001";
    /**
     * CS : �tat "provisoire"
     */
    public static final String ETAT_PR = "61170002";
    /**
     * CS : �tat "saisi"
     */
    public static final String ETAT_SA = "61170003";
    /**
     * CS : �tat "temporaire" (prestations travail pour dossier ADI)
     */
    public static final String ETAT_TMP = "61170005";
    /**
     * CS : �tat "transmis"
     */
    public static final String ETAT_TR = "61170004";

    /*
     * Mode de g�n�ration des prestation
     */
    /**
     * CS : type de g�n�ration "versement"
     */
    public static final String GENERATION_TYPE_GEN_AFFILIE = "61180002";
    /**
     * CS : type de g�n�ration "versement"
     */
    public static final String GENERATION_TYPE_GEN_DOSSIER = "61180001";
    /**
     * CS : type de g�n�ration "versement"
     */
    public static final String GENERATION_TYPE_GEN_GLOBALE = "61180003";

    /*
     * ========================================================================= groupes de codes syst�me
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
     * CS : groupe "�tat de prestation"
     * 
     * @see ALCSPrestation#ETAT_CO
     * @see ALCSPrestation#ETAT_PR
     * @see ALCSPrestation#ETAT_SA
     * @see ALCSPrestation#ETAT_TR
     */
    public static final String GROUP_ETAT = "60170000";
    /**
     * CS : groupe "type de g�n�ration"
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
