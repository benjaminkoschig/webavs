package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum RafamLegalBasis {
    LACI("02", "61320002"),
    LAFAM("01", "61320001"),
    LAI("05", "61320005"),
    LFA_MONTAGNE("04", "61320004"),
    LFA_PLAINE("03", "61320003");

    /**
     * Retourne une instance de l'enum en fonction de la base légale
     * 
     */
    public static RafamLegalBasis getLegalBasis(String code) throws JadeApplicationException {

        switch (Integer.parseInt(code)) {
            case 1:
                return LAFAM;
            case 2:
                return LACI;
            case 3:
                return LFA_PLAINE;
            case 4:
                return LFA_MONTAGNE;
            case 5:
                return LAI;

            default:
                throw new ALRafamException("RafamLegalBasis#getLegalBasis : this type is not supported");
        }
    }

    /** Code de la centrale */
    private String codeCentrale;

    /** Code système du type de prestation (RAFam) dans WebAF */
    private String cs;

    RafamLegalBasis(String codeCentrale, String cs) {
        this.cs = cs;
        this.codeCentrale = codeCentrale;
    }

    public String getCodeCentrale() {
        return codeCentrale;
    }

    public String getCS() {
        return cs;
    }
}