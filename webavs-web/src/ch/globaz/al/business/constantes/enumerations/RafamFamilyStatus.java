package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum RafamFamilyStatus {
    BEAU_PERE("21", "61380021"),
    BELLE_MERE("11", "61380011"),
    FRERE("23", "61380023"),
    GRAND_MERE("14", "61380014"),
    GRAND_PERE("24", "61380024"),
    MERE("10", "61380010"),
    MERE_NOURRICIERE("12", "61380012"),
    PERE("20", "61380020"),
    PERE_NOURRICIER("22", "61380022"),
    SOEUR("13", "61380013");

    /**
     * Retourne une instance de l'enum en fonction du statut familial
     * 
     * @param abstractFamilialStatus
     * @return
     * @throws JadeApplicationException
     */
    public static RafamFamilyStatus getFamilyStatus(String code) throws JadeApplicationException {

        switch (Integer.parseInt(code)) {
            case 10:
                return MERE;
            case 11:
                return BELLE_MERE;
            case 12:
                return MERE_NOURRICIERE;
            case 13:
                return SOEUR;
            case 14:
                return GRAND_MERE;
            case 20:
                return PERE;
            case 21:
                return BEAU_PERE;
            case 22:
                return PERE_NOURRICIER;
            case 23:
                return FRERE;
            case 24:
                return GRAND_PERE;
            default:
                throw new ALRafamException("RafamFamilyStatus#getFamilyStatus : this type is not supported");
        }
    }

    public static RafamFamilyStatus getFamilyStatusCS(String cs) throws JadeApplicationException {

        if (BEAU_PERE.getCS().equals(cs)) {
            return BEAU_PERE;
        } else if (BELLE_MERE.getCS().equals(cs)) {
            return BELLE_MERE;
        } else if (FRERE.getCS().equals(cs)) {
            return FRERE;
        } else if (GRAND_MERE.getCS().equals(cs)) {
            return GRAND_MERE;
        } else if (GRAND_PERE.getCS().equals(cs)) {
            return GRAND_PERE;
        } else if (MERE.getCS().equals(cs)) {
            return MERE;
        } else if (MERE_NOURRICIERE.getCS().equals(cs)) {
            return MERE_NOURRICIERE;
        } else if (PERE.getCS().equals(cs)) {
            return PERE;
        } else if (PERE_NOURRICIER.getCS().equals(cs)) {
            return PERE_NOURRICIER;
        } else if (SOEUR.getCS().equals(cs)) {
            return SOEUR;
        } else {
            throw new ALRafamException("RafamFamilyStatus#FamilyStatus : this type is not supported");
        }
    }

    /** Code de la centrale */
    private String codeCentrale;
    /** Code systeme */
    private String cs;

    RafamFamilyStatus(String codeCentrale, String cs) {
        this.codeCentrale = codeCentrale;
        this.cs = cs;
    }

    public String getCodeCentrale() {
        return codeCentrale;
    }

    public String getCodeLibelle() {
        return "al.enum.rafam.rafamFamilyStatus.code" + getCodeCentrale();
    }

    public String getCS() {
        return cs;
    }
}
