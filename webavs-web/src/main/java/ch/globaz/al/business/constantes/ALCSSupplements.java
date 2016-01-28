package ch.globaz.al.business.constantes;

/**
 * Codes syst�me li�s aux suppl�ments de prestations
 * 
 * <ul>
 * <li>types de suppl�ment</li>
 * 
 * @author jts
 */
public interface ALCSSupplements {
    /*
     * ========================================================================= groupes de codes syst�me
     * =========================================================================
     */

    /**
     * CS : Groupe "Types de suppl�ments"
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
     * types de suppl�ments
     */
    /**
     * CS : type conventionnel
     */
    public static final String TYPE_CONVENTIONNEL = "61260001";
    /**
     * CS : type l�gal
     */
    public static final String TYPE_LEGAL = "61260002";

}
