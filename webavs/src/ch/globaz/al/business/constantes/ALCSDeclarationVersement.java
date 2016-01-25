package ch.globaz.al.business.constantes;

/**
 * Codes systèmes liées aux déclarations de versement
 * 
 * @author pta
 * 
 */

public interface ALCSDeclarationVersement {

    /**
     * CS: déclaration sur demande
     */
    public static final String DECLA_VERS_DEMANDE = "61460001";
    /**
     * CS: déclaration paiements directs imposés à ls source
     */
    public static final String DECLA_VERS_DIR_IMP_SOURCE = "61460002";
    /**
     * CS: déclaration directs non i mposés à la source
     */
    public static final String DECLA_VERS_DIR_NON_IMP_SOUR = "61460003";
    /**
     * CS: déclaration paiements indirects frontaliers
     */
    public static final String DECLA_VERS_IND_FRONT = "61460004";
    /**
     * CS: déclaration paiements indirectes non-actifs
     */
    public static final String DECLA_VERS_IND_NON_ACT = "61460005";
    /**
     * CS: groupe des types de déclaration de versement
     */
    public static final String GROUP_DECLA_VERS_TYPE = "60460000";

}
