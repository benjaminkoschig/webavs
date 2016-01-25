package ch.globaz.al.business.constantes;

/**
 * Codes syst�me li�s aux copies
 * 
 * <ul>
 * <li>Type de copie</li>
 * </ul>
 * 
 * @author PTA
 * 
 */
public interface ALCSCopie {

    /*
     * ========================================================================= groupes de codes syst�me
     * =========================================================================
     */

    /**
     * CS : groupe "Type de copie"
     * 
     * @see ALCSCopie#TYPE_DECISION
     * @see ALCSCopie#TYPE_DECLARATION
     * @see ALCSCopie#TYPE_ECHEANCE
     */
    public static final String GROUP_COPIE_TYPE = "60270000";

    /**
     * CS: type avis de d�cision
     */
    public static final String TYPE_DECISION = "61270001";
    /**
     * CS: type declaration versement
     */
    public static final String TYPE_DECLARATION = "61270003";
    /**
     * CS: type avis d'�ch�ance
     */
    public static final String TYPE_ECHEANCE = "61270002";

}
