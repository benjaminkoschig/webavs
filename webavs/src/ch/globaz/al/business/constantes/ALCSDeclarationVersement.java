package ch.globaz.al.business.constantes;

/**
 * Codes syst�mes li�es aux d�clarations de versement
 * 
 * @author pta
 * 
 */

public interface ALCSDeclarationVersement {

    /**
     * CS: d�claration sur demande
     */
    public static final String DECLA_VERS_DEMANDE = "61460001";
    /**
     * CS: d�claration paiements directs impos�s � ls source
     */
    public static final String DECLA_VERS_DIR_IMP_SOURCE = "61460002";
    /**
     * CS: d�claration directs non i mpos�s � la source
     */
    public static final String DECLA_VERS_DIR_NON_IMP_SOUR = "61460003";
    /**
     * CS: d�claration paiements indirects frontaliers
     */
    public static final String DECLA_VERS_IND_FRONT = "61460004";
    /**
     * CS: d�claration paiements indirectes non-actifs
     */
    public static final String DECLA_VERS_IND_NON_ACT = "61460005";
    /**
     * CS: groupe des types de d�claration de versement
     */
    public static final String GROUP_DECLA_VERS_TYPE = "60460000";

}
