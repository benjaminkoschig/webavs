package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux suppléments de prestations
 * 
 * <ul>
 * <li>types de supplément</li>
 * 
 * @author jts
 */
public interface ALCSSupplements {
    /*
     * ========================================================================= groupes de codes système
     * =========================================================================
     */

    /**
     * CS : Groupe "Types de suppléments"
     * 
     * @see ALCSSupplements#TYPE_LEGAL
     * @see ALCSSupplements#TYPE_CONVENTIONNEL
     */
    public static final String GROUP_TYPE_SUPPLEMENT = "60260000";
    /*
     * =========================================================================
     * =========================================================================
     */

    /*
     * types de suppléments
     */
    /**
     * CS : type conventionnel
     */
    public static final String TYPE_CONVENTIONNEL = "61260001";
    /**
     * CS : type légal
     */
    public static final String TYPE_LEGAL = "61260002";

}
