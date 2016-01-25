package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Repr�sente les trois types de prestations possibles
 * 
 */
public enum TypePrestation {
    ABSENCES_JUSTIFIEES(68009001),
    CONGES_PAYES(68009002),
    SERVICES_MILITAIRE(68009003);

    private int codeSysteme;

    private TypePrestation(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>TypePrestation</code> associ�e au code syst�me pass� en param�tre.
     * 
     * @param value un code syst�me valide repr�sentant un type de prestation (absence justifi�e, cong� pay�, ...)
     * @return une instance valide de <code>TypePrestation</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
     */
    public static TypePrestation fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (TypePrestation etatAbsenceJustifiee : TypePrestation.values()) {
            if (etatAbsenceJustifiee.codeSysteme == intValue) {
                return etatAbsenceJustifiee;
            }
        }
        throw new GlobazTechnicalException(ExceptionMessage.UNKNOWN_VALUE_CODE_SYSTEME);
    }

    /**
     * Retourne un <code>String</code> repr�sentant le code syst�me associ�.
     * 
     * @return un <code>String</code> repr�sentant le code syst�me associ�.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

}
